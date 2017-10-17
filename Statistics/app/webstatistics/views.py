# -*- coding: utf-8 -*-
import json
from django.shortcuts import render
from django.views.generic.base import View
from models import day_url_pv_traffic
# Create your views here.


class IndexView(View):
    # 网站首页
    def get(self, request):
        day = day_url_pv_traffic.objects.all()
        url = day_url_pv_traffic.objects.all()
        traffic = day_url_pv_traffic.objects.all().order_by("-traffic_sums")[:10]
        page_view = day_url_pv_traffic.objects.all()
        avg = day_url_pv_traffic.objects.all()
        data_box = [day, url, traffic, page_view, avg]
        return render(request, 'index.html', {
            'day': day,
            'url': url,
            'traffic': traffic,
            'page_view': page_view,
            'avg': avg,
        })
