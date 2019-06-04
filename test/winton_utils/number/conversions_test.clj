(ns winton-utils.number.conversions-test
  (:require [clojure.test :refer [deftest testing is]]
           [winton-utils.number.conversions :refer [parse-number parse-integer]]))

(deftest parsing-integers
  (testing "Parsing Integer from string"
    (is (= (parse-integer "12345") 12345))
    (is (= (parse-integer "12345123451234512345") 12345123451234512345N))
    (is (= (parse-integer "1234512345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345")
           1234512345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345N))
    (is (= (parse-integer "-1234512345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345")
           -1234512345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345N))
    (is (= (parse-integer "watcha") nil))
    (is (= (parse-integer "22watcha") 22))
    (is (= (parse-integer "watcha22watcha") nil))
    ))

(deftest parsing-numbers
  (testing "Parsing number from string into integer, bigint, or double"
    (is (= (parse-number "") nil))
    (is (= (parse-number "watch") nil))
    (is (= (parse-number "1watch") 1))
    (is (= (parse-number "watch1") nil))
    (is (= (parse-number "1") 1))
    (is (= (parse-number "12345123451234512345") 12345123451234512345N))
    (is (= (parse-number "1234512345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345")
           1234512345123451234512345123451234512345123451234512345123451234512345123451234512345123451234512345N))
    (is (= (parse-number "1.") 1))
    (is (= (parse-number "1.0") 1.0))
    (is (= (parse-number "0.25") 0.25))
    (is (= (parse-number "-0.25") -0.25))
    (is (= (parse-number "0.12345678912345678") 0.12345678912345678))
    (is (= (parse-number "0.12345678912345678e-13") 1.2345678912345679E-14))
    (is (= (parse-number "1.2345678912345679E-14") 1.2345678912345679E-14))
    (is (= (parse-number "1e13") 1.0E13))
    (is (= (parse-number "1e+13") 1.0E13))
    (is (= (parse-number "-0") 0))
    (is (= (parse-number "--6") nil))

    ))