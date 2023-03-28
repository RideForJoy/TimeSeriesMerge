(ns main-test
  (:require
    [clojure.test :refer :all]
    [main.main :refer [merge-sorted-lazy]]))

(deftest merge-sorted-lazy--test
  (testing "merge-sorted-lazy test"
    (let [seq1 '("2021-07-10:10" "2021-07-14:2")
          seq2 '("2021-05-10:2" "2021-07-10:102" "2021-08-18:6")]

      (is (= '("2021-05-10:2" "2021-07-10:112" "2021-07-14:2" "2021-08-18:6")
             (merge-sorted-lazy seq1 seq2)))

      (is (= seq1
             (merge-sorted-lazy seq1 '())))

      (is (= seq2
             (merge-sorted-lazy '() seq2))))))
