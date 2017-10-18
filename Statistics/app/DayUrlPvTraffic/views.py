# -*- coding: utf-8 -*-
import json
from django.shortcuts import render
from django.views.generic.base import View
from models import DayUrlPvTraffic
# Create your views here.


class IndexView(View):
    # 网站首页
    def get(self, request):
        day = DayUrlPvTraffic.objects.all()
        url = DayUrlPvTraffic.objects.all()
        traffic = DayUrlPvTraffic.objects.all().order_by("-traffic_sums")[:10]
        page_view = DayUrlPvTraffic.objects.all()
        avg = DayUrlPvTraffic.objects.all()
        data_box = [day, url, traffic, page_view, avg]
        return render(request, 'index.html', {
            'day': day,
            'url': url,
            'traffic': traffic,
            'page_view': page_view,
            'avg': avg,
        })