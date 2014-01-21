(ns nonaga.draw.draw-rings
  (:require [nonaga.rules.rings :as r])
  (:use [nonaga.draw.util :only [hex->svg ring update-state event-type]]))

(defn ring-selected [component color coord]
  (update-state component #(assoc % :event [:ring-selected color coord])))

(defmulti draw-rings #(event-type %2))

(defmethod draw-rings :default
  [component {:keys [rings]}]
  (map (partial ring "#999") (map hex->svg rings)))

(derive :marble-moved  ::ring-selectable)
(derive :ring-selected ::ring-selectable)

(defmethod draw-rings ::ring-selectable
  [component {[t color] :event rings :rings :as state}]
  (map (fn [hex svg]
         (if (r/can-be-moved? state hex)
           (ring "#444" (ring-selected component color hex) svg)
           (ring "#999" svg)))
       rings (map hex->svg rings)))

