
> [!TIP]
> - [github.css](https://github.com/simov/markdown-viewer/blob/master/themes/github.css)

```bash
# original github.css
$ curl - fsSL -O https://github.com/simov/markdown-viewer/raw/master/themes/github.css
```

## convert markdown
```bash
# to pdf
$ pandoc -f gfm \                             # gfm - github flavored markdown
         -t html5 \
         --embed-resources --standalone \
         --metadata title="Title Here" \
         --metadata author="marslo" \         # optinoal
         --metadata date="2025-03-13" \
         --toc --toc-depth=4 \                # toc - table of contents
         --css github.css \                   # github.css, github.robot-mono.css, github.victor-mono.css, github-markdown-light.css, ...
         --css custom.css \                   # ╮ for header and footer
         -H header.html \                     # ╯
         --pdf-engine=weasyprint \            # default pdf engine
         input.md -o output.pdf

# to html
$ pandoc -f gfm -t html5 \
         --embed-resources --standalone \
         --metadata title="Title Here" \
         --metadata date="2025-03-13" \
         --toc --toc-depth=4 \
         --css github-markdown-light.css \
         --css custom.css \
         -H header.html \
         --pdf-engine=weasyprint \
         input.html -o output.html
```

### default template
```bash
# -- for html5 --
$ pandoc --print-default-template html5
# or
$ pandoc -D html5
# or
$ pandoc --print-default-data-file templates/default.html5

# -- more --
$ pandoc --print-default-data-file pptx/ppt/slideLayouts/slideLayout1.xml
$ pandoc --print-default-data-file pptx/docProps/core.xml
$ pandoc --print-default-data-file pptx/ppt/theme/theme1.xml
```

```html
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" lang="$lang$" xml:lang="$lang$"$if(dir)$ dir="$dir$"$endif$>
<head>
  <meta charset="utf-8" />
  <meta name="generator" content="pandoc" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=yes" />
$for(author-meta)$
  <meta name="author" content="$author-meta$" />
$endfor$
$if(date-meta)$
  <meta name="dcterms.date" content="$date-meta$" />
$endif$
$if(keywords)$
  <meta name="keywords" content="$for(keywords)$$keywords$$sep$, $endfor$" />
$endif$
$if(description-meta)$
  <meta name="description" content="$description-meta$" />
$endif$
  <title>$if(title-prefix)$$title-prefix$ – $endif$$pagetitle$</title>
  <style>
    $styles.html()$
  </style>
$for(css)$
  <link rel="stylesheet" href="$css$" />
$endfor$
$for(header-includes)$
  $header-includes$
$endfor$
$if(math)$
  $math$
$endif$
</head>
<body>
$for(include-before)$
$include-before$
$endfor$
$if(title)$
<header id="title-block-header">
<h1 class="title">$title$</h1>
$if(subtitle)$
<p class="subtitle">$subtitle$</p>
$endif$
$for(author)$
<p class="author">$author$</p>
$endfor$
$if(date)$
<p class="date">$date$</p>
$endif$
$if(abstract)$
<div class="abstract">
<div class="abstract-title">$abstract-title$</div>
$abstract$
</div>
$endif$
</header>
$endif$
$if(toc)$
<nav id="$idprefix$TOC" role="doc-toc">
$if(toc-title)$
<h2 id="$idprefix$toc-title">$toc-title$</h2>
$endif$
$table-of-contents$
</nav>
$endif$
$body$
$for(include-after)$
$include-after$
$endfor$
</body>
</html>
```

### list default highlight style
```bash
# support: breezedark, espresso, haddock, kate, monochrome, pygments, tango, zenburn
$ pandoc --print-highlight-style pygments
```

## Monospace Fonts

> [!TIP]
> - Regular: 400 weight, normal style (0,400)
> - Bold: 700 weight, normal style (0,700)
> - Italic: 400 weight, italic style (1,400)
> - Bold Italic: 700 weight, italic style (1,700)

- Roboto Mono
  ```css
  @import url('https://fonts.googleapis.com/css2?family=Roboto+Mono:ital,wght@0,400;0,700;1,400;1,700&display=swap');
  ```


- Source Code Pro
  ```css
  @import url('https://fonts.googleapis.com/css2?family=Source+Code+Pro:ital,wght@0,400;0,700;1,400;1,700&display=swap');
  ```

- Inconsolata
  ```css
  @import url('https://fonts.googleapis.com/css2?family=Inconsolata:ital,wght@0,400;0,700;1,400;1,700&display=swap');
  ```

- Space Mono
  ```css
  @import url('https://fonts.googleapis.com/css2?family=Space+Mono:ital,wght@0,400;0,700;1,400;1,700&display=swap');
  ```

- Anonymous Pro
  ```css
  @import url('https://fonts.googleapis.com/css2?family=Anonymous+Pro:ital,wght@0,400;0,700;1,400;1,700&display=swap');
  ```

- Ubuntu Mono
  ```css
  @import url('https://fonts.googleapis.com/css2?family=Ubuntu+Mono:ital@0;1&display=swap');
  ```

- Cousine
  ```css
  @import url('https://fonts.googleapis.com/css2?family=Cousine:ital,wght@0,400;0,700;1,400;1,700&display=swap');
  ```

- Fira Mono
  ```css
  @import url('https://fonts.googleapis.com/css2?family=Fira+Mono:ital@0;1&display=swap');
  ```

### additional style
```css
.code-sample {
  font-family: 'Source Code Pro', monospace;
}

.regular {
  font-weight: 400;
  font-style: normal;
}

.bold {
  font-weight: 700;
  font-style: normal;
}

.italic {
  font-weight: 400;
  font-style: italic;
}

.bold-italic {
  font-weight: 700;
  font-style: italic;
}
```
