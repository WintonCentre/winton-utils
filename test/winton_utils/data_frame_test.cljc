(ns winton-utils.data-frame-test
  (:require [clojure.test :refer [deftest is testing]]
            [winton-utils.data-frame :refer [map-of-vs->v-of-maps
                                             v-of-maps->map-of-vs
                                             cell-apply
                                             cell-update
                                             cell-sums
                                             cell-diffs
                                             cell-binary
                                             cell-binary-seq]]
            ))

(deftest transposing1

  (testing "map-of-vectors transposed to vector-of-maps"

    (is (= (map-of-vs->v-of-maps {:a [0 1 2] :b [10 11 12]})
           [{:a 0 :b 10} {:a 1 :b 11} {:a 2 :b 12}]))
    (is (= (map-of-vs->v-of-maps {}) []))
    (is (= (map-of-vs->v-of-maps nil) nil))

    ))

(deftest transposing2
  (testing "vector-of-maps transposed to map-of-vectors"

    (is (= {:a [0 1 2] :b [10 11 12]}
           (v-of-maps->map-of-vs [{:a 0 :b 10} {:a 1 :b 11} {:a 2 :b 12}])
           ))
    (is (= {}
           (v-of-maps->map-of-vs [])))
    (is (= {}
           (v-of-maps->map-of-vs [{}])))
    (is (= nil
           (v-of-maps->map-of-vs nil))
        )
    ))

(deftest transducer-helpers
  (testing "functions to use in transducers working over data-frames"

    (is (= (into {} (map (cell-apply inc)) {:a (range 10) :b (range 5 15)})
           {:a [1 2 3 4 5 6 7 8 9 10], :b [6 7 8 9 10 11 12 13 14 15]}))

    (is (= (into {} (map (cell-update (fn [k index old] (str k "-" index "-" old))))
                 {:a [1 2 3] :b [4 5 6]})
           {:a [":a-0-1" ":a-1-2" ":a-2-3"], :b [":b-0-4" ":b-1-5" ":b-2-6"]}))

    (is (= (into {} (map cell-sums) '([:a [1 2 3]] [:b [4 5 6]] [:c [7 8 9]] [:d [10 11 12]]))
           {:a [1 3 6], :b [4 9 15], :c [7 15 24], :d [10 21 33]}))

    (is (= (into {} (map (cell-diffs 0)) '([:a [0 1 2 3]] [:b [1 6 5 4]] [:c [3 3 3 4]] [:d [0 -1 -2]]))
           {:a [0 1 1 1], :b [1 5 -1 -1], :c [3 0 0 1], :d [0 -1 -1]}))

    (is (= (into {} (map (cell-diffs 1)) '([:a [0 1 2 3]] [:b [1 6 5 4]] [:c [3 3 3 4]] [:d [0 -1 -2]]))
           {:a [-1 1 1 1], :b [0 5 -1 -1], :c [2 0 0 1], :d [-1 -1 -1]}))

    (is (= (into {}
                 (map (cell-binary + {:a (range 10 20) :b (range 10)}))
                 {:a (range -10 -20 -1) :b (range 0 -10 -1)})
           {:a [0 0 0 0 0 0 0 0 0 0], :b [0 0 0 0 0 0 0 0 0 0]}))

    (is (= (into {}
                 (map (cell-binary-seq + (range 30 40)))
                 {:a (range -10 -20 -1) :b (range 0 -10 -1)})
           {:a [20 20 20 20 20 20 20 20 20 20], :b [30 30 30 30 30 30 30 30 30 30]}))

    (is (= (into {}
                 (map (cell-binary-seq - (range 30 40)))
                 {:a (range -10 -20 -1) :b (range 0 -10 -1)})
           {:a [40 42 44 46 48 50 52 54 56 58], :b [30 32 34 36 38 40 42 44 46 48]}))
    ))



