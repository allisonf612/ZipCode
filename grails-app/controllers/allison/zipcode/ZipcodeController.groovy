package allison.zipcode



class ZipcodeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [zipcodeInstanceList: Zipcode.list(params), zipcodeInstanceTotal: Zipcode.count()]
    }



    def show(Long id) {
        def zipcodeInstance = Zipcode.get(id)
        if (!zipcodeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'zipcode.label', default: 'Zipcode'), id])
            redirect(action: "list")
            return
        }

        [zipcodeInstance: zipcodeInstance]
    }

}
