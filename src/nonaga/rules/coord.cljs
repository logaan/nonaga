(ns nonaga.rules.coord)

(defn nw [[x y]] [(if (odd?  y) x (- x 1)) (+ y 1)])
(defn ne [[x y]] [(if (even? y) x (+ x 1)) (+ y 1)])
(defn w  [[x y]] [(- x 1) y])
(defn e  [[x y]] [(+ x 1) y])
(defn sw [[x y]] [(if (odd?  y) x (- x 1)) (- y 1)])
(defn se [[x y]] [(if (even? y) x (+ x 1)) (- y 1)])

(def directions
  [nw ne e se sw w])

(def side-by-side-directions
  [[nw ne] [ne e] [e se] [se sw] [sw w] [w nw]])

(def neighbouring-directions
  {nw [ne w]  ne [nw e]
   w  [nw sw] e  [ne se]
   sw [se w]  se [sw e]})

(defn neighbours [cell]
  (into #{} ((apply juxt directions) cell)))

(defn distance [[x1 y1] [x2 y2]]
  (let [xdiff (Math/abs (- x1 x2))
        ydiff (Math/abs (- y1 y2))
        halfy (int (/ ydiff 2))]
    (if (> xdiff halfy)
      (+ xdiff halfy)
      ydiff)))

(defn tag-with-distance [destination source]
  [(distance source destination) source])

