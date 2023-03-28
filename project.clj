(defproject tsm "1.0.0"
  :description "Time Series Merge"
  :min-lein-version "2.7.0"
  :dependencies
  [[org.clojure/clojure "1.10.1"]
   [clj-time "0.15.1"]]
  :main dev
  :profiles {:dev {:source-paths ["dev"]}}
  :test-paths ["src/test"])
