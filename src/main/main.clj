(ns main.main
  (:require
    [clj-time.format :as f]
    [clj-time.core :as t]
    [clojure.string :as str]))

(def date-formatter (f/formatter "yyyy-MM-dd"))

(defn- ^{:doc
         "Splitter for date and an integer value with separator.

         Example:
          ```
          (date-split \"2021-07-17:100\")
          => {:date \"2021-07-17\", :X 100}
          ```"}
  date-split [str]
  (update (zipmap [:date :X]
                  (str/split str #":"))
          :X read-string))

(defn- ^{:doc
         "Date parser with custom-formatter

         Example:
          ```
          (date-parse \"2021-07-17:100\")
          => #object[org.joda.time.DateTime 0x492da5e3 \"2021-07-17T00:00:00.000Z\"]
          ```"}
  date-parse [date]
  (f/parse date-formatter (:date (date-split date))))

(defn- ^{:doc
         "Summing up X values for 2 dates. Return first date

         Example:
          ```
         (date-math \"2021-07-17:100\" \"2021-07-17:7\")
          => \"2021-07-17:107\"
          ```"}
  date-math [date1 date2]
  (let [{:keys [date X]} (date-split date1)]
    (str date, ":"
         (+ X
            (-> date2
                date-split
                :X)))))

(defn ^{:doc
        "Function takes two potentially infinite lazy sequences, and returns a merged lazy sequence as the result.
         The resulting sequence follows the same formatting conventions as defined for the inputs.
         The records with the same date value merged into one by summing up their X values.

         Input parameters represented as 2 sequences of strings with the following properties:
           ● each element of a sequence is a string which contains exactly one record
           ● each record contains a date and an integer value; the records are encoded like so:\nYYYY-MM-DD:X
           ● the dates within a single sequence are non-duplicate and sorted in ascending order

        Example:
         ```
        (merge-sorted-lazy '(\"2021-07-10:10\" \"2021-07-14:2\") '(\"2021-05-10:2\" \"2021-07-10:102\" \"2021-08-18:6\"))
         => (\"2021-05-10:2\" \"2021-07-10:112\" \"2021-07-14:2\" \"2021-08-18:6\")
         ```"}
  merge-sorted-lazy
  [[head1 & tail1 :as seq1] [head2 & tail2 :as seq2]]
  (cond (some empty? [seq1 seq2]) (concat seq1 seq2)
        (t/before? (date-parse head1)
                   (date-parse head2)) (cons head1
                                             (lazy-seq (merge-sorted-lazy tail1 seq2)))
        (t/equal? (date-parse head1)
                  (date-parse head2)) (cons (date-math head1 head2)
                                            (lazy-seq (merge-sorted-lazy tail1 tail2)))
        :else (cons head2 (lazy-seq (merge-sorted-lazy seq1 tail2)))))
