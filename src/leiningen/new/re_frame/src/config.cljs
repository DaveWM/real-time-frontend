(ns {{ns-name}}.config)

(def debug?
  ^boolean goog.DEBUG)

(goog-define BACKEND_HOST "localhost")

(goog-define BACKEND_PORT 8082)

{{#git-inject?}}

(goog-define ^js/String version "unknown"){{/git-inject?}}
