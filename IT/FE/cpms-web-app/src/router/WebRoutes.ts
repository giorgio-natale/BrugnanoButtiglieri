export const WebRoutes = {
  Login: {
    buildPath: () => "/login",
    pathSchema: "/login"
  },
  Stations: {
    List: {
      buildPath: () => "/charging-station",
      pathSchema: "/charging-station"
    },
    Detail: {
      buildPath: (stationId: number) => `/charging-station/${stationId}`,
      pathSchema: `/charging-station/:stationId`
    }
  }
}