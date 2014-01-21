(ns nonaga.draw
  (:use [nonaga.react :only [circle div p svg create-class render-component]]
        [nonaga.draw.instructions :only [instructions]]
        [nonaga.draw.util :only [hex->svg ring marble update-state]]
        [nonaga.draw.marble-selected :only [draw-valid-marble-moves]]
        [nonaga.draw.draw-marbles :only [draw-marbles]]
        [nonaga.draw.draw-rings :only [draw-rings]])
  (:require [nonaga.core :as n]
            [nonaga.rules.ball :as b]
            [nonaga.rules.rings :as r])
  (:use-macros [dommy.macros :only [sel1]]))

; move-ring
(def opposite
  {:red :blue
   :blue :red})

; Same as move-marble
; draw-potential-rings
(defn move-ring [component color from to]
  (update-state component
                #(-> % (n/move-ring from to)
                     (assoc :last-ring to)
                     (assoc :event [:turn-began (opposite color)]))))

; These things should be a multi method that dispatches on state. Or I could
; have state records with a draw protocol.
; render. own ns.
(defn draw-potential-rings [component state]
  (let [[type & event-data] (:event state)
        coords (:rings state)]
    (if (= :ring-selected type)
      (let [[color source] event-data
            destinations (r/valid-destinations coords source)]
        (map (fn [hex svg]
               (ring "#DDD" (move-ring component color source hex) svg))
             destinations (map hex->svg destinations))))))

(def board
  (create-class
    "getInitialState"
    (fn []
      (this-as this
               (let [initial-state (-> n/initial-game
                                       (assoc :event [:turn-began :red]))]
                 #js {:wrapper initial-state})))
    "render"
    (fn []
      (this-as this
               (let [state (.-wrapper (.-state this)) ]
                 (div {}
                      (p {:id "instructions"}
                         (instructions (:event state)))      
                      (svg {:width 480 :height 480}
                           (draw-rings this state)
                           (draw-potential-rings this state)
                           (if-let [last-ring (:last-ring state)]
                             (ring "#000" (hex->svg last-ring)))      
                           (draw-marbles this state :red)
                           (draw-marbles this state :blue)
                           (draw-valid-marble-moves this state))))))))

(defn start []
  (render-component (board) (sel1 :#content)))

