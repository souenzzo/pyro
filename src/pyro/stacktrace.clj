(ns pyro.stacktrace
  (:require [clj-stacktrace.core :as st]
            [pyro.stacktrace.element :as element]))

(defn filter-repl
  "Given a seq of stacktrace element maps, take all elements until an
  element is found in `clojure.main/repl/read-eval-print`. If such a
  frame doesn't exist, just return the seq."
  {:added "0.1.0"}
  [st {:keys [drop-nrepl-elements]}]
  (if drop-nrepl-elements
    (take-while #(not (element/is-read-eval-print-element? (.getClassName %))) st)
    st))

(defn remove-clojure
  [st {:keys [hide-clojure-elements]}]
  (if hide-clojure-elements
    (remove #(element/is-clojure-element? (.getClassName %)) st)
    st))

(defn clean-stacktrace
  "Clean up our stacktrace."
  [st opts]
  {:added "0.1.0"}
  (let [filtered-elements (-> st
                              (filter-repl opts)
                              (remove-clojure opts))]
    (map st/parse-trace-elem filtered-elements)))
