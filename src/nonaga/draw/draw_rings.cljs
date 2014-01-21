(ns nonaga.draw.draw-rings
  (:require [nonaga.rules.rings :as r])
  (:use [nonaga.draw.util :only [hex->svg ring update-state]]))

; This is the same as start-marble-move
; draw-rings
(defn ring-selected [component color coord]
  (update-state component #(assoc % :event [:ring-selected color coord])))

; render. own ns?
(defn draw-rings [component state]
  (let [[type & event-data] (:event state)
        coords (:rings state)]
    (map (fn [hex svg]
           (if (and (or (= :marble-moved type)
                        (= :ring-selected type))
                    (r/can-be-moved? state hex))
             (let [[color] event-data]
               (ring "#444" (ring-selected component color hex) svg))
             (ring "#999" svg)))
         coords (map hex->svg coords))))

