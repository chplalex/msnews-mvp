package com.chpl.msnews.data.model

import com.google.gson.annotations.SerializedName

enum class CountryType(
    val label: String,
    val code: String
) {
    @SerializedName("Argentina")
    ARGENTINA("Argentina", "ar"),
    @SerializedName("Australia")
    AUSTRALIA("Australia", "au"),
    @SerializedName("Austria")
    AUSTRIA("Austria", "at"),
    @SerializedName("Belgium")
    BELGIUM("Belgium", "be"),
    @SerializedName("Brazil")
    BRAZIL("Brazil", "br"),
    @SerializedName("Bulgaria")
    BULGARIA("Bulgaria", "bg"),
    @SerializedName("Canada")
    CANADA("Canada", "ca"),
    @SerializedName("China")
    CHINA("China", "cn"),
    @SerializedName("Colombia")
    COLOMBIA("Colombia", "co"),
    @SerializedName("Czech Republic")
    CZECH_REPUBLIC("Czech Republic", "cz"),
    @SerializedName("Egypt")
    EGYPT("Egypt", "eg"),
    @SerializedName("France")
    FRANCE("France", "fr"),
    @SerializedName("Germany")
    GERMANY("Germany", "de"),
    @SerializedName("Greece")
    GREECE("Greece", "gr"),
    @SerializedName("Hong Kong")
    HONG("Hong Kong", "hk"),
    @SerializedName("Hungry")
    HUNGRY("Hungry", "hu"),
    @SerializedName("India")
    INDIA("India", "in"),
    @SerializedName("Indonesia")
    INDONESIA("Indonesia", "id"),
    @SerializedName("Ireland")
    IRELAND("Ireland", "ie"),
    @SerializedName("Israel")
    ISRAEL("Israel", "il"),
    @SerializedName("Italy")
    ITALY("Italy", "it"),
    @SerializedName("Japan")
    JAPAN("Japan", "jp"),
    @SerializedName("Latvia")
    LATVIA("Latvia", "lv"),
    @SerializedName("Lithuania")
    LITHUANIA("Lithuania", "lt"),
    @SerializedName("Malaysia")
    MALAYSIA("Malaysia", "my"),
    @SerializedName("Mexico")
    MEXICO("Mexico", "mx"),
    @SerializedName("Morocco")
    MOROCCO("Morocco", "ma"),
    @SerializedName("Netherlands")
    NETHERLANDS("Netherlands", "nl"),
    @SerializedName("New Zealand")
    NEW_ZEALAND("New Zealand", "nz"),
    @SerializedName("Nigeria")
    NIGERIA("Nigeria", "ng"),
    @SerializedName("Norway")
    NORWAY("Norway", "no"),
    @SerializedName("Philippines")
    PHILIPPINES("Philippines", "ph"),
    @SerializedName("Poland")
    POLAND("Poland", "pl"),
    @SerializedName("Portugal")
    PORTUGAL("Portugal", "pt"),
    @SerializedName("Romania")
    ROMANIA("Romania", "ro"),
    @SerializedName("Saudi Arabia")
    SAUDI_ARABIA("Saudi ", "sa"),
    @SerializedName("Serbia")
    SERBIA("Serbia", "rs"),
    @SerializedName("Singapore")
    SINGAPORE("Singapore", "sg"),
    @SerializedName("Slovakia")
    SLOVAKIA("Slovakia", "sk"),
    @SerializedName("Slovenia")
    SLOVENIA("Slovenia", "si"),
    @SerializedName("South Africa")
    SOUTH_AFRICA("South Africa", "za"),
    @SerializedName("South Korea")
    SOUTH_KOREA("South Korea", "kr"),
    @SerializedName("Sweden")
    SWEDEN("Sweden", "se"),
    @SerializedName("Switzerland")
    SWITZERLAND("Switzerland", "ch"),
    @SerializedName("Taiwan")
    TAIWAN("Taiwan", "tw"),
    @SerializedName("Thailand")
    THAILAND("Thailand", "th"),
    @SerializedName("Turkey")
    TURKEY("Turkey", "tr"),
    @SerializedName("UAE")
    UAE("UAE", "ae"),
    @SerializedName("Ukraine")
    UKRAINE("Ukraine", "ua"),
    @SerializedName("United Kingdom")
    UNITED_KINGDOM("United Kingdom", "gb"),
    @SerializedName("United States")
    UNITED_STATES("United States", "us"),
    @SerializedName("Venezuela")
    VENEZUELA("Venezuela", "ve"),

}
