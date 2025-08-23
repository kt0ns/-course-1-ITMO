(defn operation [op] (fn [x y] (mapv op x y)))
(def v+ (operation +))
(def v- (operation -))
(def v* (operation *))
(def vd (operation /))

(defn scalar [v1 v2] (reduce + (v* v1 v2)))
(defn vect [v1 v2] [( - (* (nth v1 1) (nth v2 2)) (* (nth v1 2) (nth v2 1)))
                    ( - (* (nth v1 2) (nth v2 0)) (* (nth v1 0) (nth v2 2)))
                    ( - (* (nth v1 0) (nth v2 1)) (* (nth v1 1) (nth v2 0)))])


;:NOTE: v*s m*s m*v должны быть реализованы через общую функцию \\fixed

(defn newTypeOp [op] (fn [x y] (mapv #(op % y) x)))


(def v*s (newTypeOp *))
(def m+ (operation v+))
(def m- (operation v-))
(def m* (operation v*))
(def md (operation vd))
(def m*s (newTypeOp v*s))
(defn transpose [m] (apply mapv vector m))
(def m*v (newTypeOp scalar))
(defn m*m [m1 m2] (mapv #(m*v (transpose m2) %) m1))


(defn tenzOp [op]
  (letfn [(f [t1 t2]
              (if (vector? t1)
                (mapv f t1 t2)
                (op t1 t2)))] f))
(def t+ (tenzOp +))
(def t* (tenzOp *))
(def t- (tenzOp -))
(def td (tenzOp /))