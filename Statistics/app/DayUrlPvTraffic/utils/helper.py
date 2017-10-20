x = [

   {'url': u'/member', 'city': u'chongqin', 'page_view': 9L},
    {'url': u'/member', 'city': u'chongqin', 'page_view': 99L},
   {'url': u'/api.php?', 'city': u'chongqin', 'page_view': 669L},
{'url': u'/api.php?', 'city': u'chongqin', 'page_view': 9L},

]


def format_data_url(data):
    """
    [{'url': u'/api.php?m', 'city': u'guangdong', 'page_view': 865L},
    {'url': u'/robots.txt', 'city': u'guangdong', 'page_view': 608L}]
    ==> {"guangdong":{'/api.php?m':865L, '/robots.txt':608L}"}
    :param data:
    :return: data_format
    """
    data_format = {}
    length = len(data)
    for i in xrange(length):
        url_pv = {}
        city_a = data[i].get("city")
        url_pv[data[i].get("url")] = data[i].get("page_view")
        for j in xrange(1, length):
            city_b = data[j].get("city")
            if city_a == city_b:
                url_pv[data[j].get("url")] = data[j].get("page_view")
                data_format[city_a] = url_pv
    return data_format


def format_data(data):
    """
    [{'url': u'/api.php?m', 'city': u'guangdong', 'page_view': 865L},
    {'url': u'/robots.txt', 'city': u'guangdong', 'page_view': 608L}]
    ==> {"guangdong":[865L, 608L]"}
    :param data:
    :return: data_format
    """
    data_format = {}

    length = len(data)
    for i in xrange(length):
        url_pv = []
        city_a = data[i].get("city")
        url_pv.append(data[i].get("page_view"))
        for j in xrange(i+1, length-1):
            city_b = data[j].get("city")
            if city_a == city_b:
                url_pv.insert(0, data[j].get("page_view"))
        data_format[city_a] = url_pv
    return data_format


print format_data(x)