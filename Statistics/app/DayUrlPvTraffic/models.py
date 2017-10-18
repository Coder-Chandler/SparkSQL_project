# This is an auto-generated Django model module.
# You'll have to do the following manually to clean this up:
#   * Rearrange models' order
#   * Make sure each model has one field with primary_key=True
#   * Make sure each ForeignKey has `on_delete` set to the desidered behavior.
#   * Remove `managed = False` lines if you wish to allow Django to create, modify, and delete the table
# Feel free to rename the models, but don't rename db_table values or field names.
from __future__ import unicode_literals

from django.db import models


class DayUrlPvTraffic(models.Model):
    day = models.CharField(max_length=8)
    url = models.CharField(max_length=1000)
    traffic_sums = models.BigIntegerField()
    page_view = models.BigIntegerField()
    avg = models.FloatField()

    class Meta:
        managed = False
        db_table = 'day_url_pv_traffic'
        unique_together = (('day', 'url'),)


class IpStatistics(models.Model):
    day = models.CharField(max_length=8)
    ip = models.CharField(max_length=100)
    traffic_sums = models.BigIntegerField()
    page_view = models.BigIntegerField()

    class Meta:
        managed = False
        db_table = 'ip_statistics'
        unique_together = (('day', 'ip'),)


class UrlCityStatistics(models.Model):
    day = models.CharField(max_length=8)
    url = models.CharField(max_length=500)
    city = models.CharField(max_length=20)
    traffic_sums = models.BigIntegerField()
    page_view = models.BigIntegerField()
    page_view_rank = models.IntegerField()

    class Meta:
        managed = False
        db_table = 'url_city_statistics'
        unique_together = (('day', 'url', 'city'),)
