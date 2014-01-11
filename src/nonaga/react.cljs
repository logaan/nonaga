(ns nonaga.react)

(defn div [props & children]
  (js/React.DOM.div (clj->js props) (clj->js children)))

(defn p [props & children]
  (js/React.DOM.p (clj->js props) (clj->js children)))

(defn svg [props & children]
  (js/React.DOM.svg (clj->js props) (clj->js children)))

(defn circle [props]
  (js/React.DOM.circle (clj->js props)))

(defn create-class [& {:as config}]
  (js/React.createClass (clj->js config)))

(defn render-component [component node]
  (js/React.renderComponent component node))

