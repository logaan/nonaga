(ns nonaga.rules.coord)

(defn nw [[x y]] [(if (odd?  y) x (- x 1)) (+ y 1)])
(defn ne [[x y]] [(if (even? y) x (+ x 1)) (+ y 1)])
(defn w  [[x y]] [(- x 1) y])
(defn e  [[x y]] [(+ x 1) y])
(defn sw [[x y]] [(if (odd?  y) x (- x 1)) (- y 1)])
(defn se [[x y]] [(if (even? y) x (+ x 1)) (- y 1)])

(def directions
  [nw ne e se sw w])

(defn neighbours [cell]
  (into #{} ((apply juxt directions) cell)))

