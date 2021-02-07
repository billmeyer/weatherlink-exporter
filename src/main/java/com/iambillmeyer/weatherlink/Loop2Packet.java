package com.iambillmeyer.weatherlink;

import java.io.PrintStream;
import java.util.Date;

/**
 * Represents a LOOP2 Package from the Vantage Serial Protocol documentation.
 * @see https://www.davisinstruments.com/support/vantage-pro-pro2-and-vue-communications-reference/
 */
public class Loop2Packet extends BaseLoopPacket
{
	public Date timestamp;
	public BarTrend barTrend;
	public double barometer;
	public double insideTemperature;
	public int insideHumidity;
	public double outsideTemperature;
	public int outsideHumidity;
	public int heatIndex;
	public int thswIndex;
	public double windSpeed;
	public double tenMinAvgWindSpeed;
	public double twoMinAvgWindSpeed;
	public double tenMinWindGust;
	public double tenMinWindGustDirection;
	public int dewPoint;
	public int windDirection;
	public double rainRate;
	public int uv;
	public int solarRadiation;
	public double stormRain;
	public int startDateCurrentStorm;
	public int dailyRain;
	public int last15MinRain;
	public int lastHourRain;
	public int dailyEt;
	public int last24HourRain;

	@Override
	public String toString()
	{
		return "LoopPacket [timestamp=" + timestamp + ", barTrend=" + barTrend + ", barometer=" + barometer + ", insideTemperature="
				+ insideTemperature + ", insideHumidity=" + insideHumidity + ", outsideTemperature="
				+ outsideTemperature + ", outsideHumidity=" + outsideHumidity + ", heatIndex=" + heatIndex
				+ ", thswIndex=" + thswIndex + ", windSpeed=" + windSpeed + ", tenMinAvgWindSpeed="
				+ tenMinAvgWindSpeed + ", twoMinAvgWindSpeed=" + twoMinAvgWindSpeed + ", tenMinWindGust="
				+ tenMinWindGust + ", tenMinWindGustDirection=" + tenMinWindGustDirection + ", dewPoint=" + dewPoint
				+ ", windDirection=" + windDirection + ", rainRate=" + rainRate + ", uv=" + uv + ", solarRadiation="
				+ solarRadiation + ", stormRain=" + stormRain + ", startDateCurrentStorm=" + startDateCurrentStorm
				+ ", dailyRain=" + dailyRain + ", last15MinRain=" + last15MinRain + ", lastHourRain=" + lastHourRain
				+ ", dailyEt=" + dailyEt + ", last24HourRain=" + last24HourRain + "]";
	}

	public void prettyPrint(PrintStream out)
	{
		out.printf("barometer = %.3f\n", barometer);
		out.printf("barTrend = %s\n", barTrend);
		out.printf("dailyEt = %d\n", dailyEt);
		out.printf("dailyRain = %d\n", dailyRain);
		out.printf("dewPoint = %d\n", dewPoint);
		out.printf("heatIndex = %d\n", heatIndex);
		out.printf("insideHumidity = %d%%\n", insideHumidity);
		out.printf("insideTemperature = %.1f\n", insideTemperature);
		out.printf("last15MinRain = %d\n", last15MinRain);
		out.printf("last24HourRain = %d\n", last24HourRain);
		out.printf("lastHourRain = %d\n", lastHourRain);
		out.printf("outsideHumidity = %d%%\n", outsideHumidity);
		out.printf("outsideTemperature = %.1f\n", outsideTemperature);
		out.printf("rainRate = %.1f\n", rainRate);
		out.printf("solarRadiation = %d\n", solarRadiation);
		out.printf("startDateCurrentStorm = %d\n", startDateCurrentStorm);
		out.printf("stormRain = %.1f\n", stormRain);
		out.printf("tenMinAvgWindSpeed = %.1f\n", tenMinAvgWindSpeed);
		out.printf("tenMinWindGust = %.1f\n", tenMinWindGust);
		out.printf("tenMinWindGustDirection = %.1f\n", tenMinWindGustDirection);
		out.printf("thswIndex = %d\n", thswIndex);
		out.printf("timestamp = %s\n", timestamp);
		out.printf("twoMinAvgWindSpeed = %.1f\n", twoMinAvgWindSpeed);
		out.printf("uv = %d\n", uv);
		out.printf("windDirection = %d\n", windDirection);
		out.printf("windSpeed = %.1f\n", windSpeed);
	}

	@Override
	public void parse(byte[] buf)
	{
		timestamp = new Date();
		barTrend = BaseLoopPacket.BarTrend.fromOrdinal(b2i(buf[3]));
		barometer = b2i(buf[7], buf[8]) / 1000.0D;
		insideTemperature = b2i(buf[9], buf[10]) / 10.0D;
		insideHumidity = b2i(buf[11]);
		outsideTemperature = b2i(buf[12], buf[13]) / 10.0D;
		windSpeed = b2i(buf[14]);
		windDirection = b2i(buf[16], buf[17]);
		tenMinAvgWindSpeed = b2i(buf[18], buf[19]) / 10.0D;
		twoMinAvgWindSpeed = b2i(buf[20], buf[21]) / 10.0D;
		tenMinWindGust = b2i(buf[22], buf[23]) / 10.0D;
		tenMinWindGustDirection = b2i(buf[24], buf[25]) / 10.0D;
		dewPoint = b2i(buf[30], buf[31]);
		outsideHumidity = b2i(buf[33]);
		heatIndex = b2i(buf[35], buf[36]);
		thswIndex = b2i(buf[37], buf[38]);
		rainRate = b2i(buf[41], buf[42]);
		uv = b2i(buf[43]);
		solarRadiation = b2i(buf[44], buf[45]);
		stormRain = b2i(buf[46], buf[47]);
		startDateCurrentStorm = b2i(buf[48], buf[49]);
		dailyRain = b2i(buf[50], buf[51]);
		last15MinRain = b2i(buf[52], buf[53]);
		lastHourRain = b2i(buf[54], buf[55]);
		dailyEt = b2i(buf[52], buf[53]);
		last24HourRain = b2i(buf[52], buf[53]);
	}

	public void zero()
	{
		barTrend = null;
		barometer = 0D;
		dailyEt = 0;
		dailyRain = 0;
		dewPoint = 0;
		heatIndex = 0;
		insideHumidity = 0;
		insideTemperature = 0D;
		last15MinRain = 0;
		last24HourRain = 0;
		lastHourRain = 0;
		outsideHumidity = 0;
		outsideTemperature = 0D;
		rainRate = 0D;
		solarRadiation = 0;
		startDateCurrentStorm = 0;
		stormRain = 0D;
		tenMinAvgWindSpeed = 0D;
		tenMinWindGust = 0D;
		tenMinWindGustDirection = 0D;
		thswIndex = 0;
		timestamp = null;
		twoMinAvgWindSpeed = 0D;
		uv = 0;
		windDirection = 0;
		windSpeed = 0D;
	}

	private int b2i(byte b)
	{
		return b & 0xFF;
	}
	
	private int b2i(byte bl, byte bh)
	{
		return (bl & 0xFF) + 256 * bh;
	}
}
