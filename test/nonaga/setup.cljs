(ns nonaga.setup)

(defn set-pre! [output]
  (let [new-text (js/document.createTextNode output)
        output (js/document.getElementById "test-output")]
    (.appendChild output new-text)))

(set-print-fn! #(set-pre! %))

