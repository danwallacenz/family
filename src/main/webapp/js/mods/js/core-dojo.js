var CORE = (function () {
    var moduleData = {},
    to_s = function (anything) { return Object.prototype.toString.call(anything); },

    debug = true;

    return {
        debug : function (on) {
            debug  = on ? true : false;
        },
        create_module : function (moduleID, creator) {
            var temp;
            if (typeof moduleID === 'string' && typeof creator === 'function') {
                temp = creator(Sandbox.create(this, moduleID));
                if (temp.init && typeof temp.init === 'function' && temp.destroy && typeof temp.destroy === 'function') {
                    temp = null;
                    moduleData[moduleID] = {
                        create : creator,
                        instance : null
                    };
                } else {
                    this.log(1, "Module '" + moduleID + "' Registration : FAILED : instance has no init or destory functions");
                }
            } else {
                this.log(1, "Module '" + moduleID + "' Registration : FAILED : one or more arguments are of incorrect type");
            }
        },
        start : function (moduleID) {
            var mod = moduleData[moduleID];
            if (mod) {
                mod.instance = mod.create(Sandbox.create(this, moduleID));
                mod.instance.init();
            }
        },
        start_all : function () {
            var moduleID;
            for (moduleID in moduleData) {
                if (moduleData.hasOwnProperty(moduleID)) {
                    this.start(moduleID);
                }
            }
        },
        stop : function (moduleID) {
            var data;
            if (data = moduleData[moduleId] && data.instance) {
                data.instance.destroy();
                data.instance = null;
            } else {
                this.log(1, "Stop Module '" + moduleID + "': FAILED : module does not exist or has not been started");
            }
        },
        stop_all : function () {
            var moduleID;
            for (moduleID in moduleData) {
                if (moduleData.hasOwnProperty(moduleID)) {
                    this.stop(moduleID);
                }
            }
        },
        registerEvents : function (evts, mod) {
            if (this.is_obj(evts) && mod) {
                if (moduleData[mod]) {
                    moduleData[mod].events = evts;
                } else {
                    this.log(1, "");
                }
            } else {
                this.log(1, "");
            }
        },
        triggerEvent : function (evt) {
            var mod;
            for (mod in moduleData) {
                if (moduleData.hasOwnProperty(mod)){
                    mod = moduleData[mod];
                    if (mod.events && mod.events[evt.type]) {
                        mod.events[evt.type](evt.data);
                    }
                }
            }
        },
        removeEvents : function (evts, mod) {
            if (this.is_obj(evts) && mod && (mod = moduleData[mod]) && mod.events) {
                delete mod.events;
            } 
        },
        log : function (severity, message) {
            if (debug) {
                console[ (severity === 1) ? 'log' : (severity === 2) ? 'warn' : 'error'](message);
            } else {
                // send to the server
            }     
        },
        dom : {
            query : function (selector, context) {
                var ret = {}, that = this, len, i =0, djEls;

                djEls = dojo.query( ((context) ? context + " " : "") + selector);

                len = djEls.length;

                while ( i < len) {
                    ret[i] = djEls[i++];
                }
                ret.length = len;
                ret.query = function (sel) {
                    return that.query(sel, selector);
                }
                return ret;
            },
            eventStore : {},
            bind : function (element, evt, fn) {
                if (element && evt) {
                    if (typeof evt === 'function') {
                        fn = evt;
                        evt = 'click';
                    }
                    if (element.length) {
                        var i = 0, len = element.length;
                        for ( ; i < len ; ) {
                            this.eventStore[element[i] + evt + fn] = dojo.connect(element[i], evt, element[i], fn);
                            i++;
                        }
                    } else {
                       this.eventStore[element + evt + fn] = dojo.connect(element, evt, element, fn);
                    }
                }
            },
            unbind : function (element, evt, fn) {
                if (element && evt) {
                    if (typeof evt === 'function') {
                        fn = evt;
                        evt = 'click';
                    }
                     if (element.length) {
                        var i = 0, len = element.length;
                        for ( ; i < len ; ) {
                            dojo.disconnect(this.eventStore[element[i] + evt + fn]);
                            delete this.eventStore[element[i] + evt + fn];
                            i++;
                        }
                    } else {
                        dojo.disconnect(this.eventStore[element + evt + fn]);
                        delete this.eventStore[element + evt + fn];
                    }
                }
            },
            create: function (el) {
                return document.createElement(el);        
            },
            apply_attrs: function (el, attrs) {
                var attr;
                for (attr in attrs) {
                    dojo.attr(el, attr, attrs[attr]);
                }
            }
        },
        is_arr : function (arr) {
            return dojo.isArray(arr);
        },
        is_obj : function (obj) {
            return dojo.isObject(obj);
        }
    };

}());
