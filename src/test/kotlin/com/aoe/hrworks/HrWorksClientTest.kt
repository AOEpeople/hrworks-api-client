package com.aoe.hrworks

import io.reactivex.observers.TestObserver
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

class HrWorksClientTest {


    companion object {
        val API_KEY = System.getenv("HRWORKS_API_KEY")
        val API_SECRET = System.getenv("HRWORKS_API_SECRET")
    }

    @Test
    fun testGetGetPresentPersonsOfOrganizationUnit() {
        val client = HrWorksClientBuilder.buildClient(
                apiKey = API_KEY,
                apiSecret = API_SECRET)

        val testObserver = TestObserver.create<PersonList>()

        client.getPresentPersonsOfOrganizationUnit(GetPresentPersonsOfOrganizationUnitRq("3"))
                .subscribe(testObserver)

        testObserver.values().forEach {
            println(it)
        }

        testObserver.assertComplete()
    }


    @Test
    fun testGetAllOrganizationUnits() {
        val client = HrWorksClientBuilder.buildClient(
                apiKey = API_KEY,
                apiSecret = API_SECRET)

        val testObserver = TestObserver.create<OrganizationUnitList>()

        client.getAllOrganizationUnits().subscribe(testObserver)

        testObserver.values().forEach {
            println(it)
        }

        testObserver.assertComplete()
    }

    @Test
    fun testGetAllActivePersons() {
        val client = HrWorksClientBuilder.buildClient(
                apiKey = API_KEY,
                apiSecret = API_SECRET)

        val testObserver = TestObserver.create<Map<String, List<Person>>>()

        client.getAllActivePersons().subscribe(testObserver)

        testObserver.values().forEach {
            println(it)
        }

        testObserver.assertComplete()
    }

    @Test
    fun testGetAllActivePersonsWithRq() {
        val client = HrWorksClientBuilder.buildClient(
                apiKey = API_KEY,
                apiSecret = API_SECRET)

        val testObserver = TestObserver.create<Map<String, List<Person>>>()

        client.getAllActivePersons(GetAllActivePersonsRq(listOf("26", "11"))).subscribe(testObserver)

        testObserver.values().forEach {
            println(it)
        }

        testObserver.assertComplete()
    }

    @Test
    fun testGetAvailableWorkingHours() {
        val client = HrWorksClientBuilder.buildClient(
                apiKey = API_KEY,
                apiSecret = API_SECRET)

        val testObserver = TestObserver.create<Map<String, List<Availability>>>()

        val request = GetAvailableWorkingHoursRq(beginDate = Date.from(LocalDate.now().minus(7, ChronoUnit.DAYS).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                endDate = Date(),
                idOrPersonnelNumberList = listOf("silas.schwarz@aoe.com"))

        client.getAvailableWorkingHours(request).subscribe(testObserver)

        testObserver.values().forEach {
            println(it)
        }

        testObserver.assertComplete()
    }

    @Test
    fun testGetAllAbsenceTypesWithoutRq() {
        val client = HrWorksClientBuilder.buildClient(
                apiKey = API_KEY,
                apiSecret = API_SECRET)

        val testObserver = TestObserver.create<AbsenceTypeList>()

        client.getAllAbsenceTypes().subscribe(testObserver)

        testObserver.values().forEach {
            println(it)
        }

        testObserver.assertComplete()
    }

    @Test
    fun testGetAllAbsenceTypesWithRq() {
        val client = HrWorksClientBuilder.buildClient(
                apiKey = API_KEY,
                apiSecret = API_SECRET)

        val testObserver = TestObserver.create<AbsenceTypeList>()

        val request = GetAllAbsenceTypesRq(onlyActive = false)

        client.getAllAbsenceTypes(request).subscribe(testObserver)

        testObserver.values().forEach {
            println(it)
        }

        testObserver.assertComplete()
    }

    @Test
    fun testGetLeaveAccountData() {
        val client = HrWorksClientBuilder.buildClient(
                apiKey = API_KEY,
                apiSecret = API_SECRET)

        val testObserver = TestObserver.create<Map<String, LeaveAccountData>>()

        val request = GetLeaveAccountDataRq(idOrNumberList = listOf("silas.schwarz@aoe.com"))

        client.getLeaveAccountData(request).subscribe(testObserver)

        testObserver.values().forEach {
            println(it)
        }

        testObserver.assertComplete()
    }


    @Test
    fun testGetAbsence() {
        val client = HrWorksClientBuilder.buildClient(
                apiKey = API_KEY,
                apiSecret = API_SECRET)

        val testObserver = TestObserver.create<Map<String, List<AbsenceData>>>()

        val request = GetAbsencesRq(beginDate = Date.from(LocalDate.now().minus(40, ChronoUnit.DAYS).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                endDate = Date(),
                idOrPersonnelNumberList = listOf("silas.schwarz@aoe.com"))

        client.getAbsences(request).subscribe(testObserver)

        testObserver.values().forEach {
            println(it)
        }

        testObserver.assertComplete()
    }

    @Test
    fun testGetAccumulatedAbsence() {
        val client = HrWorksClientBuilder.buildClient(
                apiKey = API_KEY,
                apiSecret = API_SECRET)

        val testObserver = TestObserver.create<Map<String, List<AccumulatedAbsenceData>>>()

        val request = GetAccumulatedAbsencesRq(beginDate = Date.from(LocalDate.now().minus(40, ChronoUnit.DAYS).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                endDate = Date(),
                idOrPersonnelNumberList = listOf("silas.schwarz@aoe.com"))

        client.getAccumulatedAbsences(request).subscribe(testObserver)

        testObserver.values().forEach {
            println(it)
        }

        testObserver.assertComplete()
    }

    @Test
    fun testComplexUse() {
        val client = HrWorksClientBuilder.buildClient(
                apiKey = API_KEY,
                apiSecret = API_SECRET)

        val testSubscriber = TestObserver.create<List<Map<String, List<Availability>>>>()

        val test = client.getAllActivePersons().map { allPersons ->
            allPersons.values
                    .flatten()
                    .asSequence()
                    .map { person -> person.personId }
                    .chunked(50).map { personIdList ->
                        client.getAvailableWorkingHours(GetAvailableWorkingHoursRq(
                                beginDate = Date.from(LocalDate.now().minus(1, ChronoUnit.DAYS).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                                endDate = Date(),
                                idOrPersonnelNumberList = personIdList,
                                interval = IntervalType.DAYS
                        )).blockingGet()
                    }
                    .toList()
        }.subscribe(testSubscriber)

        testSubscriber.values().forEach {
            println(it)
        }
    }

    @Test
    fun whoIsInToday() {
        val client = HrWorksClientBuilder.buildClient(
                apiKey = API_KEY,
                apiSecret = API_SECRET)

        val testObserver = TestObserver.create<Map<String, List<Absence>>>()

        client.getAllActivePersons().map { allPersons ->
            val everybody = allPersons.values
                    .flatten()
                    .asSequence()
                    .map { person -> person.personId }

            val peopleWithAbsense = everybody.chunked(50)
                    .map { personIdListChunk ->
                        client.getAbsences(GetAbsencesRq(
                                beginDate = Date(),
                                endDate = Date(),
                                idOrPersonnelNumberList = personIdListChunk,
                                interval = IntervalType.DAYS
                        )).blockingGet()
                    }.fold(mapOf<String, List<Absence>>()) { acc, currentResult ->
                        acc + (currentResult.map { resultMap -> resultMap.key to resultMap.value.flatMap { it.absences } })
                    }.toMutableMap()
            everybody.forEach { personKey ->
                peopleWithAbsense.getOrPut(personKey) { emptyList() }
            }

            peopleWithAbsense.toMap()
        }.subscribe(testObserver)

        testObserver.values().forEach {
            val howIsIn = it.entries.groupBy { availability -> availability.value.isEmpty() }
            howIsIn.entries.forEach { available ->
                when (available.key) {
                    true -> {
                        println("should be in the office today - ${available.value.size}")
                        available.value.forEach { person ->
                            println(person.key)
                        }
                        println()
                    }

                    else -> {
                        println("not in the office today - ${available.value.size}")
                        available.value.forEach { person ->
                            println("${person.key} is not in the office today because ${person.value.first()!!.name}")
                        }
                        println()
                    }
                }
            }
        }

    }


}