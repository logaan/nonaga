(ns nonaga.wrapper)

(defn svg [props child]
  (.svg (.-DOM js/React) (clj->js props) (clj->js child)))

(defn circle [props]
  (.circle (.-DOM js/React) (clj->js props)))

(defn create-class [& {:as config}]
  (.createClass js/React (clj->js config)))

(defn render-component [component node]
  (.renderComponent js/React component node))

