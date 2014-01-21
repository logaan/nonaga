(ns nonaga.draw.instructions)

(def color-name
  {:red "Red"
   :blue "Blue"})

(defmulti instructions first)

(defmethod instructions :turn-began [[type color]]
  (str (color-name color) " selects a marble to move."))

(defmethod instructions :marble-selected [[type color source]]
  (str (color-name color) " selects where to move the marble."))

(defmethod instructions :marble-moved [[type color]]
  (str (color-name color) " selects a ring to move."))

(defmethod instructions :ring-selected [[type color source]]
  (str (color-name color) " selects where to move the ring."))

