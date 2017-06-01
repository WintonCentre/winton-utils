# winton-utils

A Clojure utility library for:

* Safe number parsing

### Installation (Leiningen)

Install from git using JitPack

Add at the end of repositories in `project.clj`
```clj
:repositories [["jitpack" "https://jitpack.io"]]
```

Add the dependency
```clj
:dependencies [[com.github.WintonCentre/winton-utils "winton-utils-0.0.1"]]
```

## Usage

```clj
(ns your-ns
    (:require [number/conversions :refer [parse-number parse-int])
)
```

## License

Copyright © 2017 Mike Pearson, University of Cambridge

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
