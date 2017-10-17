from __future__ import unicode_literals

from django.db import models


# Create your models here.


class day_url_pv_traffic(models.Model):
    day = models.CharField(max_length=8, primary_key=True)
    url = models.CharField(max_length=500)
    traffic_sums = models.IntegerField(default=10)
    page_view = models.IntegerField(default=10)
    avg = models.FloatField(default=10)


