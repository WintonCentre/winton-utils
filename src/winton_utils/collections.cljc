(ns winton-utils.collections)

(defn sort-by-permutation [keyfn col]
  "works like sort-by, but returns the indices of the resulting permutation"
  (map first (sort-by (comp keyfn second) (map-indexed (fn [i e] [i e]) col)))
  )

(defn sort-permutation [col]
  "works like sort, but returns the indices of the resulting permutation"
  (map first (sort-by second (map-indexed (fn [i e] [i e]) col)))
  )


(comment

  (sort-permutation ["z" "b" "e" "a" "d" "a"])
  ;=> (3 5 1 4 2 0)

  (sort-by-permutation identity ["z" "b" "e" "a" "d" "a"])
  ;=> (3 5 1 4 2 0)

  (sort-by-permutation :a [{:a 1 :b 2} {:a 2 :b 1} {:a 3 :b 0}])
  ;=> (0 1 2)

  (sort-by-permutation :b [{:a 1 :b 2} {:a 2 :b 1} {:a 3 :b 0}])
  ;=> (2 1 0)

  )