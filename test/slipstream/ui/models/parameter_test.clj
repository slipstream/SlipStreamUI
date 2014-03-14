(ns slipstream.ui.models.parameter-test
  (:require
    [slipstream.ui.models.parameter :refer :all]
    [clojure.test :refer [deftest are is]]))

(deftest parse-parameter-test
  (are [p ans] (= ans (parse-parameter p))
               "orchestrator-machine:status" ["orchestrator-machine", nil, "status"]
               "orchestrator_machine:status" ["orchestrator_machine", nil, "status"]
               "alpha:beta" ["alpha", nil, "beta"]
               "alpha.1:beta" ["alpha", "1", "beta"]
               "alpha.2.2:beta" ["alpha.2", "2", "beta"]
               "alpha.2.2:beta.gamma" ["alpha.2", "2", "beta.gamma"]
               "alpha.2.2:beta.gamma.delta" ["alpha.2", "2", "beta.gamma.delta"]
               "non.absolute.param" [nil, nil, "non.absolute.param"]
               " orchestrator-machine:status" ["orchestrator-machine", nil, "status"]
               " alpha:beta" ["alpha", nil, "beta"]
               " alpha.1:beta" ["alpha", "1", "beta"]
               " alpha.2.2:beta" ["alpha.2", "2", "beta"]
               " alpha.2.2:beta.gamma" ["alpha.2", "2", "beta.gamma"]
               " alpha.2.2:beta.gamma.delta" ["alpha.2", "2", "beta.gamma.delta"]
               " non.absolute.param" [nil, nil, "non.absolute.param"]
               "orchestrator-machine:status " ["orchestrator-machine", nil, "status"]
               "alpha:beta " ["alpha", nil, "beta"]
               "alpha.1:beta " ["alpha", "1", "beta"]
               "alpha.2.2:beta " ["alpha.2", "2", "beta"]
               "alpha.2.2:beta.gamma " ["alpha.2", "2", "beta.gamma"]
               "alpha.2.2:beta.gamma.delta " ["alpha.2", "2", "beta.gamma.delta"]
               "non.absolute.param " [nil, nil, "non.absolute.param"]))

(deftest param-name-test
  (are [p ans] (= ans (param-name p))
               "orchestrator-machine:status" "status"
               "orchestrator_machine:status" "status"
               "alpha:beta" "beta"
               "alpha.1:beta" "beta"
               "alpha.2.2:beta" "beta"
               "alpha.2.2:beta.gamma" "beta.gamma"
               "alpha.2.2:beta.gamma.delta" "beta.gamma.delta"
               "non.absolute.param" "non.absolute.param"
               " orchestrator-machine:status" "status"
               " alpha:beta" "beta"
               " alpha.1:beta" "beta"
               " alpha.2.2:beta" "beta"
               " alpha.2.2:beta.gamma" "beta.gamma"
               " alpha.2.2:beta.gamma.delta" "beta.gamma.delta"
               " non.absolute.param" "non.absolute.param"
               "orchestrator-machine:status " "status"
               "alpha:beta " "beta"
               "alpha.1:beta " "beta"
               "alpha.2.2:beta " "beta"
               "alpha.2.2:beta.gamma " "beta.gamma"
               "alpha.2.2:beta.gamma.delta " "beta.gamma.delta"
               "non.absolute.param " "non.absolute.param"))

(deftest url-param?-test
  (are [p ans] (= ans (url-param? p))
               "orchestrator-machine:status" false
               "orchestrator_machine:status" false
               "alpha:beta" false
               "alpha.1:beta" false
               "alpha.2.2:beta" false
               "alpha.2.2:beta.gamma" false
               "alpha.2.2:beta.gamma.delta" false
               "non.abolute.param" false
               "orchestrator-machine:url.status" true
               "alpha:url.beta" true
               "alpha.1:url.beta" true
               "alpha.2.2:url.beta" true
               "alpha.2.2:url.beta.gamma" true
               "alpha.2.2:url.beta.gamma.delta" true
               "url.non.absolute.param" true
               " orchestrator-machine:status" false
               " alpha:beta" false
               " alpha.1:beta" false
               " alpha.2.2:beta" false
               " alpha.2.2:beta.gamma" false
               " alpha.2.2:beta.gamma.delta" false
               " non.abolute.param" false
               " orchestrator-machine:url.status" true
               " alpha:url.beta" true
               " alpha.1:url.beta" true
               " alpha.2.2:url.beta" true
               " alpha.2.2:url.beta.gamma" true
               " alpha.2.2:url.beta.gamma.delta" true
               " url.non.absolute.param" true
               "orchestrator-machine:status" false
               "alpha:beta " false
               "alpha.1:beta " false
               "alpha.2.2:beta " false
               "alpha.2.2:beta.gamma " false
               "alpha.2.2:beta.gamma.delta " false
               "non.abolute.param " false
               "orchestrator-machine:url.status " true
               "alpha:url.beta " true
               "alpha.1:url.beta " true
               "alpha.2.2:url.beta " true
               "alpha.2.2:url.beta.gamma " true
               "alpha.2.2:url.beta.gamma.delta " true
               "url.non.absolute.param " true))
