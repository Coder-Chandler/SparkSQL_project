# -*- coding: utf-8 -*-
import json
from django.shortcuts import render
from django.views.generic.base import View
from models import DayUrlPvTraffic, IpStatistics, UrlCityStatistics
from rest_framework.views import APIView
from rest_framework.response import Response


class HomeView(View):
    def get(self, request):
        return render(request, "html/index.html", {})


class ChartJsView(View):
    def get(self, request):
        return render(request, "html/chart_js.html", {})


class EchartsView(View):
    def get(self, request):
        return render(request, "html/echarts.html", {})


class Charts(APIView):
    authentication_classes = []
    permission_classes = []

    def get(self, request, format=None):
        # DayUrlPvTraffic
        day = DayUrlPvTraffic.objects.values("day")[:6]
        urls_orderby_traffic = DayUrlPvTraffic.objects.values("url").order_by("-traffic_sums")[:6]
        urls_orderby_pageview = DayUrlPvTraffic.objects.values("url").order_by("-page_view")[:6]

        traffics_pvtr = DayUrlPvTraffic.objects.values("traffic_sums").order_by("-traffic_sums")[:6]
        page_views_pvtr = DayUrlPvTraffic.objects.values("page_view").order_by("-page_view")[:6]

        x_url_traffic_pvtr = []
        x_url_pageview_pvtr = []

        y_traffic_pvtr = []
        y_pv_pvtr = []

        for i in traffics_pvtr:
            y_traffic_pvtr.append(i.get("traffic_sums", 0))
        for i in page_views_pvtr:
            y_pv_pvtr.append(i.get("page_view", 0))

        for j in urls_orderby_traffic:
            x_url_traffic_pvtr.append(j.get("url", "get url failed"))
        for j in urls_orderby_pageview:
            x_url_pageview_pvtr.append(j.get("url", "get url failed")[:18])

        url_traffic_pvtr_data = x_url_traffic_pvtr
        url_page_view_pvtr_data = x_url_pageview_pvtr
        traffic_pvtr_data = y_traffic_pvtr
        page_view_pvtr_data = y_pv_pvtr

        # IpStatistics
        ip_orderby_traffic = IpStatistics.objects.values("ip").order_by("-traffic_sums")[:8]
        ip_orderby_pageview = IpStatistics.objects.values("ip").order_by("-traffic_sums")[:8]

        traffic_ip = IpStatistics.objects.values("traffic_sums").order_by("-traffic_sums")[:8]
        page_views_ip = IpStatistics.objects.values("page_view").order_by("-page_view")[:8]

        # UrlCityStatistics


        data = {
            "url_traffic_data": url_traffic_pvtr_data,
            "url_page_view_data": url_page_view_pvtr_data,
            "traffic_data": traffic_pvtr_data,
            "page_view_data": page_view_pvtr_data,
        }
        return Response(data)
