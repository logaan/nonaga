(ns nonaga.wrapper)

(defn svg [props child]
  (js/React.DOM.svg (clj->js props) (clj->js child)))

(defn circle [props]
  (js/React.DOM.circle (clj->js props)))

(defn create-class [& {:as config}]
  (js/React.createClass (clj->js config)))

(defn render-component [component node]
  (js/React.renderComponent component node))

