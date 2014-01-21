(ns nonaga.draw.util
  (:use [nonaga.react :only [circle]]))

(defn event-type [state]
  (first (:event state)))

(defn hex->svg [[hex-x hex-y]]
  (let [width  40
        half (/ width 2)
        svg-x (+ 160 (* width hex-x) (if (odd? hex-y) half 0))
        svg-y (+ 160 (* width hex-y))]
    [svg-x svg-y]))


(defn ring
  ([color coord] (ring color nil coord))
  ([color click [x y :as coord]]
   (circle {"cx"          x
            "cy"          y
            "r"           14
            "fill"        "transparent"
            "stroke"      color
            "strokeWidth" "7px"
            "style"       {"cursor" (if click "pointer")}
            "onClick"     click
            "key"         (str "ring:" x "," y)})))

(defn marble
  ([color coord] (marble color nil coord))
  ([color click [x y :as coord]]
   (circle {"cx"      x
            "cy"      y
            "r"       8
            "fill"    (name color)
            "onClick" click
            "style"   {"cursor" (if click "pointer")}
            "key"     (str "marble" x "," y)})))

(defn update-state [component update-fn]
  (fn []
    (let [old-state (.-wrapper (.-state component))]
      (.setState component #js {:wrapper (update-fn old-state)}))))
