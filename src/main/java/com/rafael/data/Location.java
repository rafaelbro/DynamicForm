package com.rafael.data;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public class Location {

  private Integer latitude;
  private Integer longitude;

  public static Location fromInputString(String input) throws NumberFormatException {
    Location locale = new Location();
    String[] inputs = input.split(",");
    String trimmedLatitude = StringUtils.trim(inputs[0]);
    locale.setLatitude(Integer.valueOf(trimmedLatitude.substring(1, trimmedLatitude.length())));
    String trimmedLongitude = StringUtils.trim(inputs[1]);
    locale.setLongitude(Integer.valueOf(trimmedLongitude.substring(0, trimmedLongitude.length() - 1)));
    return locale;
  }
}
