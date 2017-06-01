(ns winton-utils.collections-test
  (:require [clojure.test :refer :all]
            [winton-utils.collections :refer [sort-permutation
                                              sort-by-permutation]]))

(deftest sort-permutations

  (testing "Sort permutations"
    (is (= (sort-permutation ["z" "b" "e" "a" "d" "a"])
           '(3 5 1 4 2 0)))

    (is (= (sort-by-permutation identity ["z" "b" "e" "a" "d" "a"])
           '(3 5 1 4 2 0)))

    (is (= (sort-by-permutation :a [{:a 1 :b 2} {:a 2 :b 1} {:a 3 :b 0}])
           '(0 1 2)))

    (is (= (sort-by-permutation :b [{:a 1 :b 2} {:a 2 :b 1} {:a 3 :b 0}])
           '(2 1 0)))


    ))
