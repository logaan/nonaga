(ns nonaga.draw
  (:use [nonaga.react :only [div p svg create-class render-component]]
        [nonaga.draw.instructions       :only [instructions]]
        [nonaga.draw.valid-marble-moves :only [draw-valid-marble-moves]]
        [nonaga.draw.marbles            :only [draw-marbles]]
        [nonaga.draw.rings              :only [draw-rings]]
        [nonaga.draw.potential-rings    :only [draw-potential-rings]]
        [nonaga.draw.last-ring          :only [draw-last-ring]])
  (:require [nonaga.core :as n]))

; To implement win detection it would be quite convenient to have the game
; state in an atom. This would allow for a win detection watcher that would
; spot that someone has one and would change the :event to end the game.

(defn game-resolve [state]
  (let [winner (b/find-winner state)] 
	(if (nil? winner)
      (str "")
	  (str (color-name winner) " wins")
	)
  ))


(defn game-resolve [state]
  (let [winner (b/find-winner state)] 
	(if (nil? winner)
      (str "")
	  (str (color-name winner) " wins")
	)
  ))


(defn game-resolve [state]
  (let [winner (b/find-winner state)] 
	(if (nil? winner)
      (str "")
	  (str (color-name winner) " wins")
	)
  ))


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
					  (p {:id "game_resolve"}
                         (game-resolve state))
                      (svg {:width 480 :height 480}
                           (draw-rings this state)
                           (draw-potential-rings this state)
                           (draw-last-ring state)
                           (draw-marbles this state :red)
                           (draw-marbles this state :blue)
                           (draw-valid-marble-moves this state))))))))

(defn start []
  (render-component (board) (js/document.getElementById "content")))

