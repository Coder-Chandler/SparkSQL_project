# -*- coding: utf-8 -*-
import json
from django.shortcuts import render
from django.views.generic.base import View
from models import DayUrlPvTraffic, IpStatistics, UrlCityStatistics
from rest_framework.views import APIView
from rest_framework.response import Response
from utils.helper import format_data


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
        ip_orderby_traffic = IpStatistics.objects.values("ip").order_by("-traffic_sums")[:6]
        ip_orderby_pageview = IpStatistics.objects.values("ip").order_by("-page_view")[:6]

        traffic_ip = IpStatistics.objects.values("traffic_sums").order_by("-traffic_sums")[:6]
        page_views_ip = IpStatistics.objects.values("page_view").order_by("-page_view")[:6]

        x_ip_traffic_ipstat = []
        x_ip_pageview_ipstat = []

        y_traffic_ipstat = []
        y_pageview_ipstat = []

        for i in ip_orderby_traffic:
            x_ip_traffic_ipstat.append(i.get("ip", "get ip failed"))
        for i in ip_orderby_pageview:
            x_ip_pageview_ipstat.append(i.get("ip", "get ip failed"))

        for j in traffic_ip:
            y_traffic_ipstat.append(j.get("traffic_sums", 0))
        for j in page_views_ip:
            y_pageview_ipstat.append(j.get("page_view", 0))

        ip_traffic_ipstat_data = x_ip_traffic_ipstat
        ip_page_view_ipstat_data = x_ip_pageview_ipstat
        traffic_ipstat_data = y_traffic_ipstat
        page_view_ipstat_data = y_pageview_ipstat

        # UrlCityStatistics
        url_city = UrlCityStatistics.objects.values("city", "url", "page_view").order_by("city", "page_view_rank")
        url_city_format = format_data(url_city)
        x_city = []
        y_pv_0 = []
        y_pv_1 = []
        y_pv_2 = []

        for k, v in url_city_format.iteritems():
            x_city.append(k)
            y_pv_0.append(v[0])
            y_pv_1.append(v[1])
            y_pv_2.append(v[2])


        data = {
            "url_traffic_data": url_traffic_pvtr_data,
            "url_page_view_data": url_page_view_pvtr_data,
            "traffic_data": traffic_pvtr_data,
            "page_view_data": page_view_pvtr_data,

            "ip_traffic_ipstat_data": ip_traffic_ipstat_data,
            "ip_page_view_ipstat_data": ip_page_view_ipstat_data,
            "traffic_ipstat_data": traffic_ipstat_data,
            "page_view_ipstat_data": page_view_ipstat_data,
        }
        return Response(data)
