const decoder = new TextDecoder();

/**
 * @typedef {Object} StreamJsonResult
 * @property {Promise} response Fetchのレスポンスを返すPromiseを返します。
 * @property {AbortController} abortController - リクエストに使用したAbortControllerを返します。これを使ってリクエストをabortできます。
 */
/**
 * `application/stream+json`によるHTTPリクエスト
 * @param {string} url URL
 * @param {object} options Fetch APIに渡すOption
 * @param {function} callback 行JSONを処理するcallback
 * @returns {StreamJsonResult} result
 */
function streamJson(url, options, callback) {
  const init = options || {};
  const abortController = new AbortController();
  const { signal } = abortController;
  init.signal = signal;
  init.headers = Object.assign(
    {
      Accept: "application/stream+json"
    },
    init.headers
  );

  // fetch開始
  const responsePromise = fetch(url, init).then(response => {
    let buffer = "";
    /**
     * ストリームから読み込まれた部分文字列を処理します
     * @param {string} chunk 読み込まれた文字列
     * @returns {void}
     */
    function consumeChunk(chunk) {
      buffer += chunk;

      // 改行コードで分割し、各JSON行を取り出します
      // https://en.wikipedia.org/wiki/JSON_streaming#Line-delimited_JSON
      // http://jsonlines.org/
      const re = /(.*?)(\r\n|\r|\n)/g;
      let result;
      let lastIndex = 0;
      while ((result = re.exec(buffer))) {
        const data = result[1];
        if (data.trim()) {
          callback(JSON.parse(data));
        }
        ({ lastIndex } = re);
      }
      // 改行コードが存在しなければ、残りの文字列は次のreadと結合して処理します
      buffer = buffer.slice(lastIndex);
    }
    // ストリームのReaderを作成
    const reader = response.body.getReader();
    // 読み込み処理本体
    function readNext() {
      return reader.read().then(({ done, value }) => {
        if (!done) {
          // --読み込み処理--
          consumeChunk(decoder.decode(value));
          return readNext();
        }

        // --終了処理--
        if (buffer.trim()) {
          // 最後に残った文字列があれば処理します。
          // 最後の行データの後ろに改行コードが含まれていなければここに到達します。
          // Spring WebFluxでは起きないようですが、 http://jsonlines.org/ を見ると最後の改行コードがない場合もある印象だったので念のため実装します。
          // `consumeChunk` は改行コードを渡さないと行を認識しないので改行コードを渡します。
          consumeChunk("\n");
        }

        return response;
      });
    }
    return readNext();
  });

  return {
    response: responsePromise,
    abortController
  };
}

export default streamJson;

/**
 * `application/stream+json`によるHTTPリクエスト
 * @param {object} vm Vue instance (インスタンス破棄と同時にfetchがキャンセルします)
 * @param {string} url URL
 * @param {object} options Fetch APIに渡すOption
 * @param {function} callback 行JSONを処理するcallback
 * @param {string} [cancelEvent] fetchをキャンセルするためのイベント名。これを指定して`vm.$emit(cancelEvent)`を呼び出すとfetchをキャンセルします
 * @returns {Promise} fetch完了Promise
 */
export function streamJsonForVue(vm, url, options, callback, cancelEvent) {
  const { abortController, response } = streamJson(url, options, callback);
  // 破棄処理とキャンセルイベントに`abortController.abort()`を登録
  const cancel = () => {
    abortController.abort();
  };
  vm.$once("hook:beforeDestroy", cancel);
  if (cancelEvent) {
    vm.$once(cancelEvent, cancel);
  }

  return response.then(res => {
    // 破棄処理の監視をoff
    vm.$off("hook:beforeDestroy", cancel);
    if (cancelEvent) {
      vm.$off(cancelEvent, cancel);
    }
    return res;
  });
}
