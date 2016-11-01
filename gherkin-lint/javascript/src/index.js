import Gherkin from "gherkin"
import LintStream from './lint_stream'
import ImplementionDetail from './rules/implementation_detail'

const parser = new Gherkin.Parser()

class GherkinLint {
  // TODO: Pass in a list of "configured" rule objects, or perhaps just pass in
  // a config object here
  constructor(enabledRuleNames) {
    this._enabledRuleNames = enabledRuleNames
  }

  // TODO: Extract bits and pieces of this to smaller methods/different class(es)
  main() {
    const paths = process.argv.slice(2)

    // TODO: Make this configurable/dynamic
    const rules = [
      new ImplementionDetail()
    ]
    const lintStream = new LintStream(rules)

    Gherkin.Stream.createSourceEventStream(paths)
      .pipe(Gherkin.Stream.createGherkinStream({printAst: true}))
      .pipe(lintStream)
      .pipe(Gherkin.Stream.createNdjsonStream())
      .pipe(process.stdout)
      //.pipe - simply into something that processes ASTs (GherkinDocument)
      // and lints. Each rule should be passed an event stream where they
      // can post events when the rule is violated. Clean and simple.
      // Could even happen in parallel. A final stream could optionally sort them.
      // This is a really clean design that would make a good blog post.
  }

  lint(path, source) {
    let errorEvents = []

    try {
      const gherkinDocument = parser.parse(source)
      if(this._enabledRuleNames[0] == 'implementation-detail') {
        const rule = new ImplementionDetail()
        const ruleErrorEvents = rule.validate(gherkinDocument, path)
        errorEvents = errorEvents.concat(ruleErrorEvents)
      }
    } catch (err) {
      // If err is a Gherkin.Errors.CompositeParserException then there are more errors on the .errors property
      const errors = err.errors || [err]
      for (const e of errors) {
        errorEvents.push({
          "type": "error",
          "source": {
            "uri": path,
            "start": e.location
          },
          "message": e.message
        })
      }
    }

    return errorEvents
  }
}

export default GherkinLint
