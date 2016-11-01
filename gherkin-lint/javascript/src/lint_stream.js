import Stream from 'stream'

class LintStream extends Stream.PassThrough {
  constructor(rules) {
    super({objectMode: true})
  }
}

export default LintStream
