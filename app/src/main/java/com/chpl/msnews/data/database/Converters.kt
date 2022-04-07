package com.chpl.msnews.data.database

import androidx.room.TypeConverter
import com.chpl.msnews.data.model.CategoryType
import com.chpl.msnews.data.model.CountryType
import com.chpl.msnews.data.model.LanguageType
import com.google.gson.Gson
import java.util.Date
import javax.inject.Inject

class Converters {

    @Inject
    lateinit var gson: Gson

    @TypeConverter
    fun mapLongToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun mapDateToLong(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun mapStringToCategory(value: String?): CategoryType? {
        return value?.let {
            try {
                gson.fromJson(value, CategoryType::class.java)
            } catch(e: Exception) {
                null
            }
        }
    }

    @TypeConverter
    fun mapCategoryToString(category: CategoryType?): String? {
        return category?.label
    }

    @TypeConverter
    fun mapStringToCountry(value: String?): CountryType? {
        return value?.let {
            try {
                gson.fromJson(value, CountryType::class.java)
            } catch(e: Exception) {
                null
            }
        }
    }

    @TypeConverter
    fun mapCountryToString(country: CountryType?): String? {
        return country?.code
    }

    @TypeConverter
    fun mapStringToLanguage(value: String?): LanguageType? {
        return value?.let {
            try {
                gson.fromJson(value, LanguageType::class.java)
            } catch(e: Exception) {
                null
            }
        }
    }

    @TypeConverter
    fun mapLanguageToString(Language: LanguageType?): String? {
        return Language?.code
    }
}