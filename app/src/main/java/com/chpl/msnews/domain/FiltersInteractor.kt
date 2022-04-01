package com.chpl.msnews.domain

import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

private val categories = listOf(
    "general",
    "business",
    "entertainment",
    "health",
    "science",
    "sports",
    "technology"
)

private val countries = listOf(
    "Argentina",
    "Australia",
    "Austria",
    "Belgium",
    "Brazil",
    "Bulgaria",
    "Canada",
    "China",
    "Colombia",
    "Czech Republic",
    "Egypt",
    "France",
    "Germany",
    "Greece",
    "Hong Kong",
    "Hungry",
    "India",
    "Indonesia",
    "Ireland",
    "Israel",
    "Italy",
    "Japan",
    "Latvia",
    "Lithuania",
    "Malaysia",
    "Mexico",
    "Morocco",
    "Netherlands",
    "New Zealand",
    "Nigeria",
    "Norway",
    "Philippines",
    "Poland",
    "Portugal",
    "Romania",
    "Saudi Arabia",
    "Serbia",
    "Singapore",
    "Slovakia",
    "Slovenia",
    "South Africa",
    "South Korea",
    "Sweden",
    "Switzerland",
    "Taiwan",
    "Thailand",
    "Turkey",
    "UAE",
    "Ukraine",
    "United Kingdom",
    "United States",
    "Venezuela"
)

// now we are getting categories and countries by hardcode
// in future we can to make remote repository, e.g. firebase storage, or to make the our own api
// for retrieve categories and countries by request
internal class FiltersInteractor
@Inject constructor() : CategoriesUseCase, CountriesUseCase {

    override fun getCategories(): Single<List<String>> {
        return Single.just(categories)
    }

    override fun getCountries(): Single<List<String>> {
        return Single.just(countries)
    }

    override fun getCountryCode(name: String): String {
        return when (name) {
            "Argentina" -> "ar"
            "Australia" -> "au"
            "Austria" -> "at"
            "Belgium" -> "be"
            "Brazil" -> "br"
            "Bulgaria" -> "bg"
            "Canada" -> "ca"
            "China" -> "cn"
            "Colombia" -> "co"
            "Czech Republic" -> "cz"
            "Egypt" -> "eg"
            "France" -> "fr"
            "Germany" -> "de"
            "Greece" -> "gr"
            "Hong Kong" -> "hk"
            "Hungry" -> "hu"
            "India" -> "in"
            "Indonesia" -> "id"
            "Ireland" -> "ie"
            "Israel" -> "il"
            "Italy" -> "it"
            "Japan" -> "jp"
            "Latvia" -> "lv"
            "Lithuania" -> "lt"
            "Malaysia" -> "my"
            "Mexico" -> "mx"
            "Morocco" -> "ma"
            "Netherlands" -> "nl"
            "New Zealand" -> "nz"
            "Nigeria" -> "ng"
            "Norway" -> "no"
            "Philippines" -> "ph"
            "Poland" -> "pl"
            "Portugal" -> "pt"
            "Romania" -> "ro"
            "Saudi Arabia" -> "sa"
            "Serbia" -> "rs"
            "Singapore" -> "sg"
            "Slovakia" -> "sk"
            "Slovenia" -> "si"
            "South Africa" -> "za"
            "South Korea" -> "kr"
            "Sweden" -> "se"
            "Switzerland" -> "ch"
            "Taiwan" -> "tw"
            "Thailand" -> "th"
            "Turkey" -> "tr"
            "UAE" -> "ae"
            "Ukraine" -> "ua"
            "United Kingdom" -> "gb"
            "United States" -> "us"
            "Venezuela" -> "ve"
            else -> ""
        }
    }
}