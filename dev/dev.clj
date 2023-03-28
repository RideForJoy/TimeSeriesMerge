(ns dev
  (:require [main.main :refer [merge-sorted-lazy]]
            [clojure.string :as str]))

(defn -main []
  (let [seq1 (str/split (read-line) #" ")
        seq2 (str/split (read-line) #" ")]
    (println (merge-sorted-lazy seq1 seq2))))
