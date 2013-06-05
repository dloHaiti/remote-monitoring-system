package com.dlohaiti.dloserver



import org.junit.*
import grails.test.mixin.*

@TestFor(ReportController)
@Mock(Report)
class ReportControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/report/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.reportInstanceList.size() == 0
        assert model.reportInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.reportInstance != null
    }

    void testSave() {
        controller.save()

        assert model.reportInstance != null
        assert view == '/report/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/report/show/1'
        assert controller.flash.message != null
        assert Report.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/report/list'

        populateValidParams(params)
        def report = new Report(params)

        assert report.save() != null

        params.id = report.id

        def model = controller.show()

        assert model.reportInstance == report
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/report/list'

        populateValidParams(params)
        def report = new Report(params)

        assert report.save() != null

        params.id = report.id

        def model = controller.edit()

        assert model.reportInstance == report
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/report/list'

        response.reset()

        populateValidParams(params)
        def report = new Report(params)

        assert report.save() != null

        // test invalid parameters in update
        params.id = report.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/report/edit"
        assert model.reportInstance != null

        report.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/report/show/$report.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        report.clearErrors()

        populateValidParams(params)
        params.id = report.id
        params.version = -1
        controller.update()

        assert view == "/report/edit"
        assert model.reportInstance != null
        assert model.reportInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/report/list'

        response.reset()

        populateValidParams(params)
        def report = new Report(params)

        assert report.save() != null
        assert Report.count() == 1

        params.id = report.id

        controller.delete()

        assert Report.count() == 0
        assert Report.get(report.id) == null
        assert response.redirectedUrl == '/report/list'
    }
}
