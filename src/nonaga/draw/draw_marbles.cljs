(ns nonaga.draw.draw-marbles
  (:use [nonaga.draw.util :only [hex->svg marble update-state]]))

(defn start-marble-move [component color coord]
  (update-state component #(assoc % :event [:marble-selected color coord])))

(defn draw-marbles [component state color]
  (let [etype  (get-in state [:event 0])
        cp     (get-in state [:event 1])
        coords (color state)
        click  (partial start-marble-move component)]
    (map (fn [hex svg]
           (marble color (if (and (or (= etype :turn-began)
                                      (= etype :marble-selected))
                                  (= cp color))
                           (click color hex)) svg))
         coords (map hex->svg coords))))

