import {FetchQueryOptions, QueryClient} from "@tanstack/react-query";
import {LoaderFunctionArgs, RouteProps} from "react-router-dom";

type getQueriesUsingLoaderArgs = (args: LoaderFunctionArgs) => Pick<FetchQueryOptions, "queryKey" | "queryFn">[]
type DataLoaderBuilderFn = (queryClient: QueryClient, getQueries: getQueriesUsingLoaderArgs) => RouteProps["loader"];

export const cachedOrFetchedDataLoader: DataLoaderBuilderFn = (queryClient, getAllQuery) =>
  async (args) => {
    const queries = getAllQuery(args);
    const queriesToFetch = queries.filter(query => !queryClient.getQueryData(query.queryKey))
    return await Promise.all(queriesToFetch.map(async query => await queryClient.fetchQuery(query)))
  }

export const alwaysFetchedDataLoader: DataLoaderBuilderFn = (queryClient, getAllQuery) =>
  async (args) => {
    const queries = getAllQuery(args);
    return await Promise.all(queries.map(async query => await queryClient.fetchQuery(query)))
  }