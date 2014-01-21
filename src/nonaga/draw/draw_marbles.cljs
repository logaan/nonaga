(ns nonaga.draw.draw-marbles
  (:use [nonaga.draw.util :only [hex->svg marble update-state event-type]]))

(defn start-marble-move [component color coord]
  (update-state component #(assoc % :event [:marble-selected color coord])))

(derive :turn-began      ::marble-selectable)
(derive :marble-selected ::marble-selectable)

(defmulti draw-marbles (fn [_ state _] (event-type state)))

(defmethod draw-marbles :default [component state color]
  (let [marbles (color state)]
    (map (partial marble color) (map hex->svg marbles))))

; Color and cp are badly named. Should be color-of-marbles-being-drawn and
; current player.
(defmethod draw-marbles ::marble-selectable
  [component {[t cp] :event :as state} color]
  (let [coords (color state)
        click  (partial start-marble-move component)]
    (map (fn [hex svg] (marble color (if (= cp color) (click color hex)) svg))
         coords (map hex->svg coords))))

