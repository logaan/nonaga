(ns nonaga.draw.marbles
  (:use [nonaga.draw.util :only [hex->svg marble update-state event-type]]))

(defn start-marble-move [component color coord]
  (update-state component #(assoc % :event [:marble-selected color coord])))

(defmulti draw-marbles (fn [_ state _] (event-type state)))

(defmethod draw-marbles :default [component state color]
  (let [marbles (color state)]
    (map (partial marble color) (map hex->svg marbles))))

(derive :turn-began      ::marble-selectable)
(derive :marble-selected ::marble-selectable)

(defmethod draw-marbles ::marble-selectable
  [component {[t current-player] :event :as state} marble-color]
  (let [coords (marble-color state)
        click  (partial start-marble-move component)]
    (map (fn [hex svg]
           (marble marble-color
                   (if (= current-player marble-color)
                     (click marble-color hex))
                   svg))
         coords (map hex->svg coords))))

