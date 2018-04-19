package com.raizlabs.dbflow5.sql.language.property

import com.raizlabs.dbflow5.BaseUnitTest
import com.raizlabs.dbflow5.converter.DateConverter
import com.raizlabs.dbflow5.converter.TypeConverter
import com.raizlabs.dbflow5.models.Difficulty
import com.raizlabs.dbflow5.models.EnumTypeConverterModel_Table
import com.raizlabs.dbflow5.models.SimpleModel
import com.raizlabs.dbflow5.query.NameAlias
import com.raizlabs.dbflow5.query.property.TypeConvertedProperty
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*
import kotlin.reflect.KClass

class TypeConvertedPropertyTest : BaseUnitTest() {


    @Test
    fun testTypeConverter() {
        val property = TypeConvertedProperty<Long, Date>(SimpleModel::class, "Prop", true,
                object : TypeConvertedProperty.TypeConverterGetter {
                    override fun getTypeConverter(modelClass: KClass<*>): TypeConverter<*, *> = DateConverter()
                })
        assertEquals("`Prop`", property.toString())

        val date = Date()
        assertEquals("`Prop`=${date.time}", property.eq(date).query)

        assertEquals("`SimpleModel`.`Prop`=${date.time}", property.withTable().eq(date).query)

        val inverted = property.invertProperty()
        assertEquals("`Prop`=5050505", inverted.eq(5050505).query)
    }

    @Test
    fun testCustomEnumTypeConverter() {

        assertEquals("`difficulty`='H'", EnumTypeConverterModel_Table.difficulty.eq(Difficulty.HARD).query)
        assertEquals("`EnumTypeConverterModel`.`difficulty`='H'", EnumTypeConverterModel_Table.difficulty.withTable().eq(Difficulty.HARD).query)
        assertEquals("`et`.`difficulty`='H'", EnumTypeConverterModel_Table.difficulty.withTable(NameAlias.tableNameBuilder("et").build()).eq(Difficulty.HARD).query)
    }
}
