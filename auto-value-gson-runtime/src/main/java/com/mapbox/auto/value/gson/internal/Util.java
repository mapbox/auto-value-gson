/*
 * Copyright (C) 2014 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mapbox.auto.value.gson.internal;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public final class Util {
  static final Type[] EMPTY_TYPE_ARRAY = new Type[] {};

  private Util() {
  }

  public static Map<String, String> renameFields(Class<?> targetClass,
      List<String> names,
      FieldNamingStrategy fieldNamingStrategy) {
    Map<String, String> renamedFields = new LinkedHashMap<>();
    for (String fieldName : names) {
      if (fieldNamingStrategy instanceof FieldNamingPolicy) {
        switch ((FieldNamingPolicy) fieldNamingStrategy) {
          case UPPER_CAMEL_CASE:
            renamedFields.put(fieldName, upperCaseFirstLetter(fieldName));
            break;
          case UPPER_CAMEL_CASE_WITH_SPACES:
            renamedFields.put(fieldName, upperCaseFirstLetter(separateCamelCase(fieldName, " ")));
            break;
          case LOWER_CASE_WITH_UNDERSCORES:
            renamedFields.put(fieldName, separateCamelCase(fieldName, "_").toLowerCase(Locale.ENGLISH));
            break;
          case LOWER_CASE_WITH_DASHES:
            renamedFields.put(fieldName, separateCamelCase(fieldName, "-").toLowerCase(Locale.ENGLISH));
            break;
          case LOWER_CASE_WITH_DOTS:
            renamedFields.put(fieldName, separateCamelCase(fieldName, ".").toLowerCase(Locale.ENGLISH));
            break;
          default:
            renamedFields.put(fieldName, fieldName);
        }
      } else {
        try {
          renamedFields.put(fieldName, fieldNamingStrategy.translateName(targetClass.getDeclaredField(fieldName)));
        } catch (NoSuchFieldException E) {
          renamedFields.put(fieldName, fieldName);
        }
      }
    }
    return renamedFields;
  }
  private static String separateCamelCase(String name, String separator) {
    StringBuilder translation = new StringBuilder();
    for (int i = 0, length = name.length(); i < length; i++) {
      char character = name.charAt(i);
      if (Character.isUpperCase(character) && translation.length() != 0) {
        translation.append(separator);
      }
      translation.append(character);
    }
    return translation.toString();
  }

  private static String modifyString(char firstCharacter, String srcString,
      int indexOfSubstring) {
    return (indexOfSubstring < srcString.length()) ? firstCharacter + srcString.substring(indexOfSubstring) : String.valueOf(firstCharacter);
  }

  private static String upperCaseFirstLetter(String name) {
    StringBuilder fieldNameBuilder = new StringBuilder();
    int index = 0;
    char firstCharacter = name.charAt(index);
    int length = name.length();
    while (index < length - 1) {
      if (Character.isLetter(firstCharacter)) {
        break;
      }
      fieldNameBuilder.append(firstCharacter);
      firstCharacter = name.charAt(++index);
    }
    if (!Character.isUpperCase(firstCharacter)) {
      String modifiedTarget = modifyString(Character.toUpperCase(firstCharacter), name, ++index);
      return fieldNameBuilder.append(modifiedTarget).toString();
    } else {
      return name;
    }
  }

  /**
   * Returns a type that is functionally equal but not necessarily equal according to {@link
   * Object#equals(Object) Object.equals()}.
   */
  private static Type canonicalize(Type type) {
    if (type instanceof Class) {
      Class<?> c = (Class<?>) type;
      return c.isArray() ? new GenericArrayTypeImpl(canonicalize(c.getComponentType())) : c;

    } else if (type instanceof ParameterizedType) {
      if (type instanceof ParameterizedTypeImpl) return type;
      ParameterizedType p = (ParameterizedType) type;
      return new ParameterizedTypeImpl(p.getOwnerType(),
          p.getRawType(), p.getActualTypeArguments());

    } else if (type instanceof GenericArrayType) {
      if (type instanceof GenericArrayTypeImpl) return type;
      GenericArrayType g = (GenericArrayType) type;
      return new GenericArrayTypeImpl(g.getGenericComponentType());

    } else if (type instanceof WildcardType) {
      if (type instanceof WildcardTypeImpl) return type;
      WildcardType w = (WildcardType) type;
      return new WildcardTypeImpl(w.getUpperBounds(), w.getLowerBounds());

    } else {
      return type; // This type is unsupported!
    }
  }

  private static int hashCodeOrZero(@Nullable Object o) {
    return o != null ? o.hashCode() : 0;
  }

  private static String typeToString(Type type) {
    return type instanceof Class ? ((Class<?>) type).getName() : type.toString();
  }

  private static void checkNotPrimitive(Type type) {
    if ((type instanceof Class<?>) && ((Class<?>) type).isPrimitive()) {
      throw new IllegalArgumentException("Unexpected primitive " + type + ". Use the boxed type.");
    }
  }

  public static final class ParameterizedTypeImpl implements ParameterizedType {
    private final @Nullable Type ownerType;
    private final Type rawType;
    final Type[] typeArguments;

    ParameterizedTypeImpl(@Nullable Type ownerType, Type rawType, Type... typeArguments) {
      // Require an owner type if the raw type needs it.
      if (rawType instanceof Class<?>) {
        Class<?> enclosingClass = ((Class<?>) rawType).getEnclosingClass();
        if (ownerType != null) {
          if (enclosingClass == null || WildcardUtil.getRawType(ownerType) != enclosingClass) {
            throw new IllegalArgumentException(
                "unexpected owner type for " + rawType + ": " + ownerType);
          }
        } else if (enclosingClass != null) {
          throw new IllegalArgumentException(
              "unexpected owner type for " + rawType + ": null");
        }
      }

      this.ownerType = ownerType == null ? null : canonicalize(ownerType);
      this.rawType = canonicalize(rawType);
      this.typeArguments = typeArguments.clone();
      for (int t = 0; t < this.typeArguments.length; t++) {
        if (this.typeArguments[t] == null) throw new NullPointerException();
        checkNotPrimitive(this.typeArguments[t]);
        this.typeArguments[t] = canonicalize(this.typeArguments[t]);
      }
    }

    @Override public Type[] getActualTypeArguments() {
      return typeArguments.clone();
    }

    @Override public Type getRawType() {
      return rawType;
    }

    @Override public @Nullable Type getOwnerType() {
      return ownerType;
    }

    @Override public boolean equals(Object other) {
      return other instanceof ParameterizedType
          && WildcardUtil.equals(this, (ParameterizedType) other);
    }

    @Override public int hashCode() {
      return Arrays.hashCode(typeArguments)
          ^ rawType.hashCode()
          ^ hashCodeOrZero(ownerType);
    }

    @Override public String toString() {
      StringBuilder result = new StringBuilder(30 * (typeArguments.length + 1));
      result.append(typeToString(rawType));

      if (typeArguments.length == 0) {
        return result.toString();
      }

      result.append("<").append(typeToString(typeArguments[0]));
      for (int i = 1; i < typeArguments.length; i++) {
        result.append(", ").append(typeToString(typeArguments[i]));
      }
      return result.append(">").toString();
    }
  }

  public static final class GenericArrayTypeImpl implements GenericArrayType {
    private final Type componentType;

    GenericArrayTypeImpl(Type componentType) {
      this.componentType = canonicalize(componentType);
    }

    @Override public Type getGenericComponentType() {
      return componentType;
    }

    @Override public boolean equals(Object o) {
      return o instanceof GenericArrayType
          && WildcardUtil.equals(this, (GenericArrayType) o);
    }

    @Override public int hashCode() {
      return componentType.hashCode();
    }

    @Override public String toString() {
      return typeToString(componentType) + "[]";
    }
  }

  /**
   * The WildcardType interface supports multiple upper bounds and multiple lower bounds. We only
   * support what the Java 6 language needs - at most one bound. If a lower bound is set, the upper
   * bound must be Object.class.
   */
  public static final class WildcardTypeImpl implements WildcardType {
    private final Type upperBound;
    private final @Nullable Type lowerBound;

    WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
      if (lowerBounds.length > 1) throw new IllegalArgumentException();
      if (upperBounds.length != 1) throw new IllegalArgumentException();

      if (lowerBounds.length == 1) {
        if (lowerBounds[0] == null) throw new NullPointerException();
        checkNotPrimitive(lowerBounds[0]);
        if (upperBounds[0] != Object.class) throw new IllegalArgumentException();
        this.lowerBound = canonicalize(lowerBounds[0]);
        this.upperBound = Object.class;

      } else {
        if (upperBounds[0] == null) throw new NullPointerException();
        checkNotPrimitive(upperBounds[0]);
        this.lowerBound = null;
        this.upperBound = canonicalize(upperBounds[0]);
      }
    }

    @Override public Type[] getUpperBounds() {
      return new Type[] { upperBound };
    }

    @Override public Type[] getLowerBounds() {
      return lowerBound != null ? new Type[] { lowerBound } : EMPTY_TYPE_ARRAY;
    }

    @Override public boolean equals(Object other) {
      return other instanceof WildcardType
          && WildcardUtil.equals(this, (WildcardType) other);
    }

    @Override public int hashCode() {
      // This equals Arrays.hashCode(getLowerBounds()) ^ Arrays.hashCode(getUpperBounds()).
      return (lowerBound != null ? 31 + lowerBound.hashCode() : 1)
          ^ (31 + upperBound.hashCode());
    }

    @Override public String toString() {
      if (lowerBound != null) {
        return "? super " + typeToString(lowerBound);
      } else if (upperBound == Object.class) {
        return "?";
      } else {
        return "? extends " + typeToString(upperBound);
      }
    }
  }
}
