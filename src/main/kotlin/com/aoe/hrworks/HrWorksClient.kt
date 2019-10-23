package com.aoe.hrworks

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.Date

interface HrWorksClient {

    @POST("/")
    @Headers("${HrWorksClientBuilder.HEADER_HR_WORKS_TARGET}: GetPresentPersonsOfOrganizationUnit")
    fun getPresentPersonsOfOrganizationUnit(@Body request: GetPresentPersonsOfOrganizationUnitRq): Single<PersonList>

    @POST("/")
    @Headers("${HrWorksClientBuilder.HEADER_HR_WORKS_TARGET}: GetAllOrganizationUnits")
    fun getAllOrganizationUnits(): Single<OrganizationUnitList>

    @POST("/")
    @Headers("${HrWorksClientBuilder.HEADER_HR_WORKS_TARGET}: GetAllPermanentEstablishments")
    fun getAllPermanentEstablishments(): Single<List<PermanentEstablishment>>

    @POST("/")
    @Headers("${HrWorksClientBuilder.HEADER_HR_WORKS_TARGET}: GetHolidays")
    fun getHolidays(@Body request: GetHolidaysRq): Single<Map<String, HolidayData>>

    @POST("/")
    @Headers("${HrWorksClientBuilder.HEADER_HR_WORKS_TARGET}: GetAllActivePersons")
    fun getAllActivePersons(@Body request: GetAllActivePersonsRq): Single<Map<String, List<Person>>>

    @POST("/")
    @Headers("${HrWorksClientBuilder.HEADER_HR_WORKS_TARGET}: GetAllActivePersons")
    fun getAllActivePersons(): Single<Map<String, List<Person>>>

    @POST("/")
    @Headers("${HrWorksClientBuilder.HEADER_HR_WORKS_TARGET}: GetAvailableWorkingHours")
    fun getAvailableWorkingHours(@Body request: GetAvailableWorkingHoursRq): Single<Map<String, List<Availability>>>

    @POST("/")
    @Headers("${HrWorksClientBuilder.HEADER_HR_WORKS_TARGET}: GetPersonMasterData")
    fun getPersonMasterData(@Body request: GetPersonMasterDataRq): Single<Map<String, List<PersonMasterData>>>

    @POST("/")
    @Headers("${HrWorksClientBuilder.HEADER_HR_WORKS_TARGET}: GetAllAbsenceTypes")
    fun getAllAbsenceTypes(): Single<AbsenceTypeList>

    @POST("/")
    @Headers("${HrWorksClientBuilder.HEADER_HR_WORKS_TARGET}: GetAllAbsenceTypes")
    fun getAllAbsenceTypes(@Body request: GetAllAbsenceTypesRq): Single<AbsenceTypeList>

    @POST("/")
    @Headers("${HrWorksClientBuilder.HEADER_HR_WORKS_TARGET}: GetLeaveAccountData")
    fun getLeaveAccountData(@Body request: GetLeaveAccountDataRq): Single<Map<String, LeaveAccountData>>

    @POST("/")
    @Headers("${HrWorksClientBuilder.HEADER_HR_WORKS_TARGET}: GetAbsences")
    fun getAbsences(@Body request: GetAbsencesRq): Single<Map<String, List<AbsenceData>>>

    @POST("/")
    @Headers("${HrWorksClientBuilder.HEADER_HR_WORKS_TARGET}: GetAbsences")
    fun getAccumulatedAbsences(@Body request: GetAccumulatedAbsencesRq): Single<Map<String, List<AccumulatedAbsenceData>>>

}

data class PersonList(
    val persons: List<Person>
)

data class OrganizationUnitList(
    val organizationUnits: List<OrganizationUnit>
)

data class PermanentEstablishment(
    val name: String,
    val id: String
)

data class HolidayData(
    val permamentEstablishmentHolidays: Map<String, List<Holiday>>,
    val stateHolidays: Map<String, List<Holiday>>,
    val generalHolidays: List<Holiday>
)

data class Holiday(
    val date: Date,
    val isHalfDay: Boolean,
    val name: String
)

data class AbsenceTypeList(
    val absenceTypes: List<AbsenceType>
)

data class OrganizationUnit(
    val organizationUnitNumber: String,
    val organizationUnitName: String
)

data class AbsenceData(
    val beginDate: Date,
    val endDate: Date,
    val absences: List<Absence>
)

data class AccumulatedAbsenceData(
    val beginDate: Date,
    val endDate: Date,
    val absences: Map<String, Double>
)

data class Availability(
    val beginDate: Date,
    val endDate: Date,
    val workingHours: Double
)

data class PersonMasterData(
    val personnelNumber: String,
    val personId: String,
    val personLicenseNumber: String,
    val firstName: String,
    val lastName: String,
    val title: String,
    val email: String,
    val gender: String,
    val position: String,
    val address: Address,
    val probationEndDate: String,
    val highestLevelOfEducation: String,
    val highestProfessionalQualification: String,
    val superior: Person,
    val organizationUnit: OrganizationUnit,
    val birthday: Date,
    val secondNationality: String,
    val joinDate: Date,
    val companyMobilePhoneNumber: String,
    val officePhoneNumber: String,
    val socialSecurityNumber: String,
    val countryCode: String,
    val workSchedule: WorkSchedule,
    val costCentre: CostUnit,
    val bankAccount: BankAccount,
    val taxpayerIdentificationNumber: String,
    val buildingOrRoom: String,
    val costObject: CostUnit,
    val permanentEstablishment: PermanentEstablishment
)

data class Address(
    val street: String,
    val additionalData: String,
    val streetNumber: String,
    val countryCode: String,
    val zipCode: String,
    val city: String
)

data class WorkSchedule(
    val name: String,
    val workingDays: List<Map<String, WorkingDay>>
)

data class WorkingDay(
    val workingHours: Double,
    val day: String
)

data class CostUnit(
    val number: String,
    val name: String
)

data class BankAccount(
    val bic: String,
    val iban: String
)

data class LeaveAccountData(
    val holidayEntitlement: Double,
    val requested: Double,
    val approved: Double,
    val unplanned: Double,
    val planned: Double
)

data class Person(
    val personnelNumber: String,
    val personId: String,
    val firstName: String,
    val lastName: String
)

data class AbsenceType(
    val name: String,
    val key: String,
    val type: String,
    val isActive: Boolean,
    val reducesHolidayEntitlement: Boolean
)

data class Absence(
    val name: String,
    val absenceTypeKey: String,
    val beginDate: Date,
    val endDate: Date,
    val status: String,
    val workingDays: String
)

data class GetPresentPersonsOfOrganizationUnitRq(
    val organizationUnitNumber: String
)

data class GetHolidaysRq(
    val year: Int,
    val countryCodes: List<String>? = null,
    val permanentEstablishments: List<String>? = null
)

data class GetAllActivePersonsRq(
    val organizationUnits: List<String>
)

data class GetAvailableWorkingHoursRq(
    val beginDate: Date,
    val endDate: Date,
    @SerializedName("persons")
    val idOrPersonnelNumberList: List<String>,
    val interval: IntervalType? = null,
    val usePersonnelNumbers: Boolean = false
)

data class GetPersonMasterDataRq(
    val persons: List<String>,
    val usePersonnelNumbers: Boolean? = null
)

data class GetAllAbsenceTypesRq(
    val onlyActive: Boolean = true
)

data class GetLeaveAccountDataRq(
    val referenceDate: Date = Date(),
    @SerializedName("persons") val idOrNumberList: List<String>,
    val usePersonnelNumbers: Boolean = false
)

data class GetAbsencesRq(
    val beginDate: Date,
    val endDate: Date,
    @SerializedName("persons")
    val idOrPersonnelNumberList: List<String>,
    val interval: IntervalType? = null,
    val usePersonnelNumbers: Boolean = false
) {
    val count = false
}

data class GetAccumulatedAbsencesRq(
    val beginDate: Date,
    val endDate: Date,
    @SerializedName("persons")
    val idOrPersonnelNumberList: List<String>,
    val interval: IntervalType? = null,
    val usePersonnelNumbers: Boolean = false
) {
    val count = true
}
