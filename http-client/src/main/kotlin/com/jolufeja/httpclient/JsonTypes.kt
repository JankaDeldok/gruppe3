package com.jolufeja.httpclient

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.type.TypeFactory


typealias JsonTypeSupplier<@Suppress("unused", "UNUSED_TYPEALIAS_PARAMETER") T> =
            (typeFactory: TypeFactory) -> JavaType

fun <T : Any> jsonTypeOf(clazz: Class<T>): JsonTypeSupplier<T> =
    { tf -> tf.constructType(clazz) }


fun <T : Any> jsonTypeOf(typeRef: TypeReference<T>): JsonTypeSupplier<T> =
    { tf -> tf.constructType(typeRef) }


inline fun <reified T : Any> jsonTypeOf(): JsonTypeSupplier<T> =
    jsonTypeOf(T::class.java)


inline fun <reified T : Any> jsonTypeOfGeneric(): JsonTypeSupplier<T> =
    jsonTypeOf(object : TypeReference<T>() {})


fun <T : Any> jsonListOf(elementType: Class<T>): JsonTypeSupplier<List<T>> =
    { tf -> tf.constructCollectionType(List::class.java, elementType) }


fun <T : Any> jsonListOf(elementTypeSupplier: JsonTypeSupplier<T>): JsonTypeSupplier<List<T>> =
    { tf -> tf.constructCollectionType(List::class.java, elementTypeSupplier(tf)) }


inline fun <reified T : Any> jsonListOf(): JsonTypeSupplier<List<T>> =
    jsonListOf(T::class.java)


fun <K : Any, V : Any> jsonMapOf(keyType: Class<K>, valueType: Class<V>): JsonTypeSupplier<Map<K, V>> =
    { tf -> tf.constructMapType(Map::class.java, keyType, valueType) }


fun <K : Any, V : Any> jsonMapOf(
    keyTypeSupplier: JsonTypeSupplier<K>, valueTypeSupplier: JsonTypeSupplier<V>
): JsonTypeSupplier<Map<K, V>> =
    { tf -> tf.constructMapType(Map::class.java, keyTypeSupplier(tf), valueTypeSupplier(tf)) }


fun <K : Any, V : Any> jsonMapOf(
    keyType: Class<K>, valueTypeSupplier: JsonTypeSupplier<V>
): JsonTypeSupplier<Map<K, V>> =
    { tf -> tf.constructMapType(Map::class.java, tf.constructType(keyType), valueTypeSupplier(tf)) }


inline fun <reified K : Any, reified V : Any> jsonMapOf() =
    jsonMapOf(K::class.java, V::class.java)