package de.scandio.e4.testpackages.pqconf.util

import de.scandio.e4.confluence.web.WebConfluence
import de.scandio.e4.helpers.DomHelper
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.support.ui.Select
import java.util.*

class PocketQuerySeleniumHelper(
        val webConfluence: WebConfluence,
        val dom: DomHelper
) {

    val DATASOURCE_TYPE_REST_CUSTOM = "4"

    fun createSqlDatasource(name: String, url: String, user: String, pwd: String): String {
        //"jdbc:mysql://localhost/world"

        var driverString: String = ""
        if (url.contains("mysql")) {
            driverString = "com.mysql.jdbc.Driver"
        }

        goToAdminSection("database")
        openAddEntityForm("database")
        val datasourceName = "${name}_${Date().time}"
        insertEntityName("database", datasourceName)
        dom.insertText("#database-url", url)
        dom.click("#database-driver")
        dom.awaitHasValue("#database-driver", driverString)
        dom.insertText("#database-user", user)
        dom.insertText("#database-password", pwd)
        dom.click("#pocket-databases a.testconnection")
        dom.awaitHasText("#pocket-databases .nice-right .nice-status", "success")
        submitForm("database", datasourceName)
        return datasourceName
    }


    fun createRestCustomDatasource(name: String, url: String): String {
        goToAdminSection("database")
        openAddEntityForm("database")
        val datasourceName = "${name}_${Date().time}"
        insertEntityName("database", datasourceName)
        webConfluence.takeScreenshot("createRestDatasource-1")
        dom.setSelectedOption("#database-type", DATASOURCE_TYPE_REST_CUSTOM)
        webConfluence.takeScreenshot("createRestDatasource-2")
        dom.insertText("#database-url", url)
        webConfluence.takeScreenshot("createRestDatasource-3")
        dom.click("#pocket-databases a.testconnection")
        dom.awaitHasText("#pocket-databases .nice-right .nice-status", "success")
        webConfluence.takeScreenshot("createRestDatasource-4")
        submitForm("database", datasourceName)
        return datasourceName
    }

    fun createRestQuery(datasourceName: String, name: String, url: String, jsonPath: String): String {
        val queryName = createBaseQuery(datasourceName, name, url)
        dom.insertText("#query-jsonpath", jsonPath)
        submitForm("query", queryName)
        return queryName
    }

    fun createSqlQuery(datasourceName: String, name: String, statementOrUrl: String): String {
        val queryName = createBaseQuery(datasourceName, name, statementOrUrl)
        submitForm("query", queryName)
        return queryName
    }

    fun createBaseQuery(datasourceName: String, name: String, statementOrUrl: String): String {
        goToAdminSection("query")
        openAddEntityForm("query")
        val queryName = "${name}_${Date().time}"
        insertEntityName("query", queryName)
        dom.setSelectedOption("#query-database", datasourceName)
        setEditorValue(statementOrUrl)
        return queryName
    }

    fun goToPocketQueryAdmin() {
        webConfluence.navigateTo("plugins/servlet/pocketquery/admin")
        dom.awaitElementPresent("#pocket-admin")
    }

    fun goToAdminSection(entityType: String) {
        if (!webConfluence.driver.currentUrl.contains("pocketquery/admin")) {
            goToPocketQueryAdmin()
        }
        dom.click("li[data-section='$entityType'] a")
        dom.awaitClass("#pocket-${pluralEntityType(entityType)}", "active")
    }

    fun openAddEntityForm(entityType: String) {
        dom.click("#pocket-${pluralEntityType(entityType)} .nice-add-entity")
        dom.awaitClass("#pocket-${pluralEntityType(entityType)}", "form-visible")
    }

    fun openEditEntityForm(entityType: String, entityName: String) {
        dom.click("#pocket-${pluralEntityType(entityType)} li[data-entityname='$entityName']")
        dom.awaitClass("#pocket-${pluralEntityType(entityType)}", "form-visible")
    }

    fun insertEntityName(entityType: String, entityName: String) {
        dom.insertText("#$entityType-name", entityName)
    }

    fun pluralEntityType(entityType: String): String {
        var entityTypePlural = "${entityType}s"
        if (entityType == "query") {
            entityTypePlural = "queries"
        }
        return entityTypePlural
    }

    fun submitForm(entityType: String, entityName: String) {
        dom.click("#pocket-${pluralEntityType(entityType)} .nice-right input.submit")
        dom.awaitNoClass("#pocket-${pluralEntityType(entityType)}", "form-visible")
    }

    fun awaitEntityPresent(entityType: String, entityName: String) {
        dom.awaitElementPresent("#${pluralEntityType(entityType)}-list li[data-entityname='$entityName']")
    }

    fun setEditorValue(value: String) {
        val js = webConfluence.driver as JavascriptExecutor
        js.executeScript("arguments[0].CodeMirror.setValue(\"$value\");", dom.findElement(".CodeMirror"))
    }

    private fun createTemplateOrConverter(entityType: String, name: String, content: String): String {
        goToAdminSection(entityType)
        openAddEntityForm(entityType)
        val fullEntityName = "${name}_${Date().time}"
        insertEntityName(entityType, fullEntityName)
        setEditorValue(content)
        submitForm(entityType, fullEntityName)
        return fullEntityName
    }

    fun createTemplate(name: String, content: String): String {
        return createTemplateOrConverter("template", name, content)
    }

    fun createConverter(name: String, content: String): String {
        return createTemplateOrConverter("converter", name, content)
    }

    private fun setLinkedEntity(sourceEntityType: String, sourceEntityName: String, targetEntityType: String, targetEntityName: String) {
        goToAdminSection(sourceEntityType)
        openEditEntityForm(sourceEntityType, sourceEntityName)
        dom.setSelectedOption("#$sourceEntityType-$targetEntityType", targetEntityName)
        submitForm(sourceEntityType, sourceEntityName)
        dom.awaitMilliseconds(50)
    }

    fun setTemplateOnQuery(queryName: String, templateName: String) {
        setLinkedEntity("query", queryName, "template", templateName)
    }

    fun setConverterOnQuery(queryName: String, converterName: String) {
        setLinkedEntity("query", queryName, "converter", converterName)
    }

    fun setQueryInMacroBrowser(queryName: String) {
        dom.executeScript("$('#query-name-select').val('$queryName').trigger('change')")
        dom.awaitMilliseconds(50)
    }

    fun insertPocketQueryMacro(outerQueryName: String) {
        webConfluence.openMacroBrowser("pocketquery", "pocketquery")
        setQueryInMacroBrowser(outerQueryName)
        webConfluence.saveMacroBrowser()
    }

}